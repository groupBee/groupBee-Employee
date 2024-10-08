package groupbee.employee.service.department;

import groupbee.employee.dto.department.DepartmentDto;
import groupbee.employee.mapper.DepartmentMapper;
import groupbee.employee.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    public final DepartmentRepository departmentRepository;
    public final DepartmentMapper departmentMapper;

    public List<DepartmentDto> findAll(){
        return departmentMapper.listDto(departmentRepository.findAll());
    }
}
