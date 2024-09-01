package groupbee.employee.service.employee;

import groupbee.employee.dto.employee.EmployeeDto;
import groupbee.employee.mapper.EmployeeMapper;
import groupbee.employee.repository.EmployeeRepository;
import groupbee.employee.service.xml.OdooClient;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.xmlrpc.XmlRpcException;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class HRService {
    private final HttpSession httpSession;
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;


    public EmployeeDto getEmployeeDto() {
        return employeeMapper.toDto(
                employeeRepository.findByPotalId(
                        httpSession.getAttribute(
                                FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME)
                                .toString())
        );
    }

    public Map<String,Object> getInfo() throws MalformedURLException, XmlRpcException {
        EmployeeDto employeeDto = getEmployeeDto();
        String email = employeeDto.getEmail();
        Map<String,Object> data = (Map<String, Object>) Objects.requireNonNull(OdooClient.employeeInfo(email))[0];
        data.put("memberId",employeeDto.getId());
        return data;
    }
}
