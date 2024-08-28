package groupbee.employee.service.employee;

import groupbee.employee.LoginStatusEnum;
import groupbee.employee.StatusEnum;
import groupbee.employee.dto.email.EmailDto;
import groupbee.employee.dto.employee.EmployeeDetailDto;
import groupbee.employee.dto.employee.EmployeeDto;
import groupbee.employee.dto.employee.EmployeeListDto;
import groupbee.employee.dto.ldap.LdapDto;
import groupbee.employee.entity.EmailEntity;
import groupbee.employee.entity.EmployeeEntity;
import groupbee.employee.mapper.EmailMapper;
import groupbee.employee.mapper.EmployeeMapper;
import groupbee.employee.repository.EmailRepository;
import groupbee.employee.repository.EmployeeRepository;
import groupbee.employee.service.feign.MailFeignClient;
import groupbee.employee.service.redis.RedisService;
import groupbee.employee.service.session.SessionService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.stereotype.Service;

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
    private final MailFeignClient mailFeignClient;
    private final Map<String, Object> response = new HashMap<>();

    @Transactional
    public void save(EmployeeDto dto){
        employeeRepository.save(employeeMapper.toEntity(dto));
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> sync (List<LdapDto> ldapDtos){
        Map<String, Object> response = new HashMap<>();
        try {
            ldapDtos.forEach(ldapDto -> {
                EmployeeDto employeeDto = EmployeeDto.builder()
                        .id(ldapDto.getAttributes().get("ipaUniqueID").toString())
                        .potalId(ldapDto.getAttributes().get("uid").toString())
                        .name(ldapDto.getAttributes().get("cn").toString())
                        .position((Long) ldapDto.getAttributes().get("employeeType"))
                        .email(ldapDto.getAttributes().get("mail").toString())
                        .extensionCall( ldapDto.getAttributes().get("telephoneNumber").toString())
                        .phoneNumber( ldapDto.getAttributes().get("mobile").toString())
                        .address(ldapDto.getAttributes().get("street").toString())
                        .departmentId(Long.valueOf(ldapDto.getAttributes().get("departmentNumber").toString()))
                        .build();
                if(employeeRepository.countById(employeeDto.getId()) == 0){
                    Map<String, Object> data = new HashMap<>();
                    employeeDto.setMembershipStatus(true);
                    employeeDto.setIsAdmin(false);
                    employeeDto.setPasswd(encoder.encode("p@ssw0rd"));
                    employeeRepository.save(employeeMapper.toEntity(employeeDto));
                    data.put("local_part",employeeDto.getPotalId());
                    data.put("domain","groupbee.co.kr");
                    data.put("name",employeeDto.getName());
                    data.put("quota","1024");
                    data.put("password","p@ssw0rd");
                    data.put("password2","p@ssw0rd");
                    data.put("active","1");
                    data.put("force_pw_update","0");
                    data.put("tls_enforce_in","1");
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
            httpSession.setAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME,potalId);
            response.put("status", LoginStatusEnum.OK);
            response.put("isAdmin",employeeRepository.findByPotalId(potalId).getIsAdmin());
            return ResponseEntity.status(200).body(response);
        } else {
            response.put("status", LoginStatusEnum.BAD_PASSWORD);
            httpSession.invalidate();
            return ResponseEntity.status(402).body(response);
        }
    }

    @Transactional
    public ResponseEntity<Map<String,Object>> getIsLogin(){
        Map<String, Object> response = new HashMap<>();
        if(httpSession.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME) == null){
            httpSession.invalidate();
            return ResponseEntity.status(400).body(response);
        }
        response.put("status", LoginStatusEnum.OK);
        response.put("isAdmin",employeeRepository.findByPotalId(httpSession.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME).toString()).getIsAdmin());
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
        String passwd = employeeRepository.findByPotalId(httpSession.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME).toString()).getPasswd();
        if(encoder.matches(data.get("old").toString(),passwd)){
            Map<String, Object> mailData = new HashMap<>();
            employeeRepository.updateByPasswd(encoder.encode(data.get("new").toString()),httpSession.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME).toString());
            mailData.put("items", Arrays.asList("email",employeeRepository.findByPotalId(httpSession.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME).toString()).getEmail()));
            mailData.put("attr",Arrays.asList("password",data.get("new").toString(),"password2",data.get("new").toString()));
            mailFeignClient.editMailbox(mailData);
            emailRepository.updateByPasswd(data.get("new").toString(),employeeRepository.findByPotalId(httpSession.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME).toString()).getEmail());
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
    public ResponseEntity<Map<String, Object>> update(EmployeeDto employeeDto) {
        Map<String, Object> response = new HashMap<>();
        if(employeeRepository.countById(employeeDto.getId()) == 0){
            response.put("status", "fail");
            response.put("message", "사용자가 존재하지 않습니다.");
            return ResponseEntity.status(401).body(response);
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

}
