package groupbee.employee.service.employee;

import groupbee.employee.dto.EmployeeDto;
import groupbee.employee.mapper.EmployeeMapper;
import groupbee.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public void save(EmployeeDto dto){
        employeeRepository.save(employeeMapper.toEntity(dto));
    }

    public void checkLongin(String id, String passwd){

    }

    public void delete(String id){

    }

}
