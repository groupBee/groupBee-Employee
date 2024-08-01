package groupbee.employee.mapper;

import groupbee.employee.dto.DepartmentDto;
import groupbee.employee.dto.EmployeeDto;
import groupbee.employee.entity.DepartmentEntity;
import groupbee.employee.entity.EmployeeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    DepartmentEntity toEntity(DepartmentDto departmentDto);
    @Mapping(target = "department.id", source = "departmentId")
    EmployeeEntity toEntity(EmployeeDto employeeDto);
}
