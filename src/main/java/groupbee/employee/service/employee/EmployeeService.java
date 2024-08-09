package groupbee.employee.service.employee;

import groupbee.employee.dto.EmployeeDto;
import groupbee.employee.dto.LdapDto;
import groupbee.employee.entity.EmployeeEntity;
import groupbee.employee.mapper.EmployeeMapper;
import groupbee.employee.repository.EmployeeRepository;
import groupbee.employee.service.redis.RedisService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.stereotype.Service;

import java.beans.Beans;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final BCryptPasswordEncoder encoder;
    private final HttpSession httpSession;
    private final RedisService redisService;
    private final Map<String, Object> response = new HashMap<>();

    @Transactional
    public void save(EmployeeDto dto){
        employeeRepository.save(employeeMapper.toEntity(dto));
    }

    @Transactional
    public Map<String, Object> sync (List<LdapDto> ldapDtos){
        Map<String, Object> response = new HashMap<>();
        try {
            ldapDtos.forEach(ldapDto -> {
                EmployeeDto employeeDto = EmployeeDto.builder()
                        .id(ldapDto.getAttributes().get("ipaUniqueID").toString())
                        .potalId(ldapDto.getAttributes().get("uid").toString())
                        .name(ldapDto.getAttributes().get("cn").toString())
                        .position(ldapDto.getAttributes().get("employeeType").toString())
                        .email(ldapDto.getAttributes().get("mail").toString())
                        .extensionCall( ldapDto.getAttributes().get("telephoneNumber").toString())
                        .phoneNumber( ldapDto.getAttributes().get("mobile").toString())
                        .address(ldapDto.getAttributes().get("street").toString())
                        .departmentId(Long.valueOf(ldapDto.getAttributes().get("departmentNumber").toString()))
                        .build();
                if(employeeRepository.countById(employeeDto.getId()) == 0){
                    employeeDto.setMembershipStatus(true);
                    employeeDto.setPasswd(encoder.encode("p@ssw0rd"));
                    employeeRepository.save(employeeMapper.toEntity(employeeDto));
                } else {
                    employeeDto.setPasswd(employeeRepository.findByPotalId(employeeDto.getPotalId()).getPasswd());
                    employeeRepository.save(employeeMapper.toEntity(employeeDto));
                }
            });
            response.put("status","success");
            response.put("message","동기화가 완료되었습니다.");
        } catch (Exception e) {
            response.put("status","fail");
            response.put("message","동기화가 실패하였습니다.");
            e.printStackTrace();
        }
        return response;
    }
    @Transactional
    public Map<String,Object> checkLongin(String potalId, String passwd){
        Map<String,Object> response = new HashMap<>();
        if (employeeRepository.findByPotalId(potalId) == null){
            response.put("status","success");
            response.put("data",false);
            response.put("message","아이디가 존재하지 않습니다.");
            return response;
        }
        if(encoder.matches(passwd,employeeRepository.findByPotalId(potalId).getPasswd())){
            httpSession.setAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME,potalId);
            response.put("status","success");
            response.put("data",httpSession.getId());
            response.put("message","로그인 성공");
        } else {
            response.put("status","success");
            response.put("data",false);
            response.put("message","비밀번호가 일치하지 않습니다.");
        }
        return response;
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
    public Map<String,Object> logout(){
        if (httpSession.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME) == null) {
            response.put("status", "fail");
            response.put("message", "로그인이 필요합니다.");
            return response;
        }
        httpSession.removeAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME);
        response.put("status", "success");
        response.put("message", "로그아웃 성공");
        return response;
    }

    @Transactional
    public Map<String, Object> update(EmployeeDto employeeDto) {
        Map<String, Object> response = new HashMap<>();
        if(employeeRepository.countById(employeeDto.getId()) == 0){
            response.put("status", "fail");
            response.put("message", "사용자가 존재하지 않습니다.");
            return response;
        }
        EmployeeEntity entity = employeeMapper.toEntity(employeeDto);
        entity.setPasswd(encoder.encode(employeeDto.getPasswd() == null ?
                employeeRepository.findByPotalId(employeeDto.getPotalId()).getPasswd() :
                employeeDto.getPasswd()));
        employeeRepository.save(entity);
        response.put("status", "success");
        response.put("message", "사용자가 정보가 변경되었습니다..");
        return response;
    }

    public Map<String, Object> getEmployeeInfo() {
        if (httpSession.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME) == null) {
            response.put("status", "fail");
            response.put("data", null);
            response.put("message", "로그인이 필요합니다.");
            return response;
        }
        String id = httpSession.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME).toString();
        EmployeeEntity entity = employeeRepository.findByPotalId(id);
        if (entity == null) {
            response.put("status", "fail");
            response.put("data", null);
            response.put("message", "존재하지 않는 사용자입니다.");
            return response;
        }

        response.put("status", "success");
        response.put("data", employeeData(entity));
        response.put("message", "조회 성공");
        return response;
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

}
