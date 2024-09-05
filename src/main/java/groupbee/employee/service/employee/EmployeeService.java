package groupbee.employee.service.employee;

import groupbee.employee.LoginStatusEnum;
import groupbee.employee.StatusEnum;
import groupbee.employee.dto.email.EmailDto;
import groupbee.employee.dto.employee.EmployeeDetailDto;
import groupbee.employee.dto.employee.EmployeeDto;
import groupbee.employee.dto.employee.EmployeeListDto;
import groupbee.employee.dto.employee.EmployeeUpdateDto;
import groupbee.employee.dto.ldap.LdapDto;
import groupbee.employee.entity.EmailEntity;
import groupbee.employee.entity.EmployeeEntity;
import groupbee.employee.mapper.EmailMapper;
import groupbee.employee.mapper.EmployeeMapper;
import groupbee.employee.repository.EmailRepository;
import groupbee.employee.repository.EmployeeRepository;
import groupbee.employee.service.feign.MailFeignClient;
import groupbee.employee.service.feign.RocketChatFeignClient;
import groupbee.employee.service.minio.MinioService;
import groupbee.employee.service.redis.RedisService;
import groupbee.employee.service.session.SessionService;
import groupbee.employee.service.xml.OdooClient;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.xmlrpc.XmlRpcException;
import org.hibernate.Hibernate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final BCryptPasswordEncoder encoder;
    private final HttpSession httpSession;
    private final RedisService redisService;
    private final EmailRepository emailRepository;
    private final EmailMapper emailMapper;
    private final SessionService sessionService;
    private final RocketChatFeignClient rocketChatFeignClient;
    private final MailFeignClient mailFeignClient;
    private final MinioService minioService;
    @Transactional
    public void save(EmployeeDto dto){
        employeeRepository.save(employeeMapper.toEntity(dto));
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> sync (List<LdapDto> ldapDtos){
        Map<String, Object> response = new HashMap<>();
        try {
            ldapDtos.forEach(ldapDto -> {
                String company_name;
                String identification_id;
                String first;
                String barcode;
                String email = ldapDto.getAttributes().get("mail").toString();
                System.out.println(email);
                try {
                    Map<String, Object> data = (Map<String, Object>) Objects.requireNonNull(OdooClient.employeeInfo(email))[0];
                    identification_id = (String) data.get("identification_id");
                    barcode = (String) data.get("barcode");
                    first = (String) data.get("first_contract_date");
                    company_name = (Arrays.asList((Object[]) data.get("company_id"))).get(1).toString();
                } catch (MalformedURLException | XmlRpcException e) {
                    throw new RuntimeException(e);
                }
                EmployeeDto employeeDto = EmployeeDto.builder()
                        .id(ldapDto.getAttributes().get("ipaUniqueID").toString())
                        .potalId(ldapDto.getAttributes().get("uid").toString())
                        .name(ldapDto.getAttributes().get("cn").toString())
                        .position(Long.valueOf(ldapDto.getAttributes().get("employeeType").toString()))
                        .email(ldapDto.getAttributes().get("mail").toString())
                        .extensionCall(ldapDto.getAttributes().get("telephoneNumber").toString())
                        .phoneNumber(ldapDto.getAttributes().get("mobile").toString())
                        .address(ldapDto.getAttributes().get("street").toString())
                        .email(ldapDto.getAttributes().get("mail").toString())
                        .companyName(company_name)
                        .residentRegistrationNumber(identification_id)
                        .firstDay(first)
                        .idNumber(barcode)
                        .departmentId(Long.valueOf(ldapDto.getAttributes().get("departmentNumber").toString()))
                        .build();
                if (employeeRepository.countById(employeeDto.getId()) == 0) {
                    Map<String, Object> data = new HashMap<>();
                    employeeDto.setMembershipStatus(true);
                    employeeDto.setIsAdmin(false);
                    employeeDto.setPasswd(encoder.encode("p@ssw0rd"));
                    employeeRepository.save(employeeMapper.toEntity(employeeDto));
                    data.put("local_part", employeeDto.getPotalId());
                    data.put("domain", "groupbee.co.kr");
                    data.put("name", employeeDto.getName());
                    data.put("quota", "1024");
                    data.put("password", "p@ssw0rd");
                    data.put("password2", "p@ssw0rd");
                    data.put("active", "1");
                    data.put("force_pw_update", "0");
                    data.put("tls_enforce_in", "1");
                    Map<String, Object> rocketData = new HashMap<>();
                    rocketData.put("username", employeeDto.getPotalId());
                    rocketData.put("email", employeeDto.getEmail());
                    rocketData.put("password", "p@ssw0rd");
                    rocketData.put("name", employeeDto.getName());
                    rocketData.put("active", true);
                    mailFeignClient.addMailbox(data);
                    rocketChatFeignClient.register(rocketData);

                    EmailEntity emailEntity = EmailEntity.builder()
                            .email(employeeDto.getEmail())
                            .password("p@ssw0rd")
                            .build();
                    emailRepository.save(emailEntity);
                } else {
                    employeeRepository.updateWithoutPasswd(employeeMapper.toEntity(employeeDto));
                }
            });
            response.put("status",StatusEnum.OK);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            response.put("status","Failed");
            e.printStackTrace();
            return ResponseEntity.status(500).body(response);
        }
    }
    @Transactional
    public ResponseEntity<Map<String,Object>> checkLongin(String potalId, String passwd){
        Map<String, Object> response = new HashMap<>();
        if (employeeRepository.findByPotalId(potalId) == null){
            response.put("status", LoginStatusEnum.BAD_ID);
            httpSession.invalidate();
            return ResponseEntity.status(401).body(response);
        }
        if(encoder.matches(passwd,employeeRepository.findByPotalId(potalId).getPasswd())){
            if(!employeeRepository.findByPotalId(potalId).getMembershipStatus()){
                response.put("status", LoginStatusEnum.NOT_FOUND);
                httpSession.invalidate();
                return ResponseEntity.status(403).body(response);
            }
            try{
                Map<String, Object> rocketData = new HashMap<>();
                rocketData.put("user",potalId);
                rocketData.put("password",emailRepository.findByEmail(employeeRepository.findByPotalId(potalId).getEmail()).getPassword());
                httpSession.setAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME,potalId);
                response.put("status", LoginStatusEnum.OK);
                response.put("rocketData",rocketChatFeignClient.login(rocketData));
                response.put("isAdmin",employeeRepository.findByPotalId(potalId).getIsAdmin());
                return ResponseEntity.status(200).body(response);
            } catch (Exception e){
                e.printStackTrace();
                response.put("status", LoginStatusEnum.NOT_FOUND);
                httpSession.invalidate();
                return ResponseEntity.status(403).body(response);
            }
        } else {
            response.put("status", LoginStatusEnum.NOT_FOUND);
            httpSession.invalidate();
            return ResponseEntity.status(401).body(response);
        }
    }

    @Transactional
    public ResponseEntity<Map<String,Object>> getIsLogin(){
        Map<String, Object> response = new HashMap<>();
        if(httpSession.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME) == null){
            httpSession.invalidate();
            return ResponseEntity.status(400).body(response);
        }
        String potalId = httpSession.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME).toString();
        response.put("status", LoginStatusEnum.OK);
        response.put("isAdmin",employeeRepository.findByPotalId(potalId).getIsAdmin());
        return ResponseEntity.status(200).body(response);
    }

    @Transactional
    public Map<String,Object> delete(String id){
        Map<String, Object> response = new HashMap<>();
        if(employeeRepository.countById(id) == 0){
            response.put("status", "fail");
            response.put("message", "사용자를 찾을 수 없습니다.");
            return response;
        }
        employeeRepository.deleteByPotalId(id);
        response.put("status", "success");
        response.put("message", "사용자가 삭제되었습니다.");
        return response;
    }

    @Transactional
    public ResponseEntity<Map<String,Object>> updatePassword(Map<String,Object> data){
        Map<String, Object> response = new HashMap<>();
        String potalId = httpSession.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME).toString();
        String passwd = employeeRepository.findByPotalId(potalId).getPasswd();
        String oldPass = data.get("old").toString();
        String newPass = data.get("new").toString();
        if(encoder.matches(oldPass,passwd)){
            Map<String, Object> mailData = new HashMap<>();
            employeeRepository.updateByPasswd(encoder.encode(newPass),potalId);
            mailData.put("items", Map.of("email",employeeRepository.findByPotalId(potalId).getEmail()));
            mailData.put("attr", Map.of("password",newPass,"password2",newPass));
            mailFeignClient.editMailbox(mailData);
            Map<String,Object> rocketData = new HashMap<>();
            rocketData.put("userId",httpSession.getAttribute("rocketUserId").toString());
            rocketData.put("data",Map.of("password",newPass));
            rocketChatFeignClient.update(rocketData);
            emailRepository.updateByPasswd(newPass,employeeRepository.findByPotalId(potalId).getEmail());
            response.put("status", StatusEnum.OK);
            return ResponseEntity.status(200).body(response);
        }
        response.put("status", StatusEnum.BAD_REQUEST);
        return ResponseEntity.status(400).body(response);
    }

    @Transactional
    public ResponseEntity<Map<String,Object>> logout(){
        Map<String, Object> response = new HashMap<>();
        if (httpSession.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME) == null) {
            response.put("status", StatusEnum.BAD_REQUEST);
            httpSession.invalidate();
            return ResponseEntity.status(400).body(response);
        }
        httpSession.removeAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME);
        httpSession.invalidate();
        response.put("status", StatusEnum.OK);
        return ResponseEntity.status(200).body(response);
    }

    @Transactional
    public ResponseEntity<EmailDto> getEmail(){
        Map<String, Object> response = new HashMap<>();
        EmailDto emailDto = new EmailDto();
        if (httpSession.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME) == null) {
            response.put("status", StatusEnum.BAD_REQUEST);
            return ResponseEntity.status(400).body(null);
        }
        String email = employeeRepository.findByPotalId(httpSession.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME).toString()).getEmail();
        emailDto = emailMapper.toDto(emailRepository.findByEmail(email));
        return ResponseEntity.status(200).body(emailDto);
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> update(EmployeeDto employeeDto, MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        if(employeeRepository.countById(employeeDto.getId()) == 0){
            response.put("status", "fail");
            response.put("message", "사용자가 존재하지 않습니다.");
            return ResponseEntity.status(401).body(response);
        }
        if(file != null && !file.isEmpty()) {
            employeeDto.setProfileFile("https://minio.bmops.kro.kr/groupbee/profile/"+minioService.uploadFile("groupbee","profile",file));
        }
        EmployeeEntity entity = employeeMapper.toEntity(employeeDto);
        employeeRepository.updateAll(entity);
        response.put("status", "success");
        response.put("message", "사용자가 정보가 변경되었습니다..");
        return ResponseEntity.status(200).body(response);
    }

    @Transactional
    public ResponseEntity<EmployeeDetailDto> getEmployeeInfo() {
        // 세션에서 사용자의 포털 ID를 가져옵니다.
        String portalId = (String) httpSession.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME);
        if (portalId == null) {
            httpSession.invalidate();
            return ResponseEntity.status(400).body(null);
        }

        // 포털 ID를 사용하여 데이터베이스에서 사용자의 ID를 찾습니다.
        EmployeeDto employee = employeeMapper.toDto(employeeRepository.findByPotalId(portalId));
        if (employee == null || employee.getId() == null) {
            httpSession.invalidate();
            return ResponseEntity.status(401).body(null);
        }

        String id = employee.getId();
        System.out.println(id);

        // 사용자의 ID를 사용하여 상세 정보를 가져옵니다.

        EmployeeDetailDto employeeDetail = employeeRepository.findDetailById(id);
        // 필요한 경우 추가 필드를 초기화
        Hibernate.initialize(employeeDetail.getDepartment()); // 예시로 department 초기화
        Hibernate.initialize(employeeDetail.getPosition());   // 예시로 position 초기화


        return ResponseEntity.status(200).body(employeeDetail);
    }

    @Transactional
    public ResponseEntity<List<EmployeeListDto>> getEmployeeList() {
        if(httpSession.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME) == null){
            httpSession.invalidate();
            return ResponseEntity.status(400).body(null);
        }
        List<EmployeeListDto> response = employeeRepository.findListAll();
        return ResponseEntity.status(200).body(response);
    }
    public Map<String,Object> employeeData(EmployeeEntity entity){
        Map<String,Object> response = new HashMap<>();
        response.put("id",entity.getId());
        response.put("company_name",entity.getCompanyName());
        response.put("potal_id",entity.getPotalId());
        response.put("name",entity.getName());
        response.put("resident_registration_number",entity.getResidentRegistrationNumber());
        response.put("position",entity.getPosition());
        response.put("email",entity.getEmail());
        response.put("extension_call",entity.getExtensionCall());
        response.put("phone_number",entity.getPhoneNumber());
        response.put("address",entity.getAddress());
        response.put("membership_status",entity.getMembershipStatus());
        response.put("departmentName",entity.getDepartment().getDepartmentName());
        response.put("departmentNumber",entity.getDepartment().getId());
        return response;
    }

    @Transactional
    public ResponseEntity<EmployeeDetailDto> getEmployeeDetail(String id) {
        if(httpSession.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME) == null){
            httpSession.invalidate();
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.status(200).body(employeeRepository.findDetailById(id));
    }

    public void updateRocketChatSession(Map<String,Object> data) {
        httpSession.setAttribute("rocketUserId",data.get("userId"));
        httpSession.setAttribute("rocketAuthToken",data.get("authToken"));
    }

    public ResponseEntity<Map<String,Object>> resetPassword(String id) {
        String potalId = httpSession.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME).toString();
        if(!employeeRepository.findByPotalId(potalId).getIsAdmin()){
            httpSession.invalidate();
            return ResponseEntity.status(400).body(null);
        }
        Map<String, Object> response = new HashMap<>();
        employeeRepository.updateByPasswd(encoder.encode("p@ssw0rd"),id);
        emailRepository.updateByPasswd("p@ssw0rd",employeeRepository.findByPotalId(id).getEmail());
        response.put("status", StatusEnum.OK);
        return ResponseEntity.status(200).body(response);
    }
}
