package groupbee.employee.mapper;


import groupbee.employee.dto.EmployeeDto;
import groupbee.employee.entity.EmployeeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(source = "departmentId", target = "department.id")
    EmployeeEntity toEntity(EmployeeDto dto);

    @Mapping(source = "department.id", target = "departmentId")
    EmployeeDto toDto(EmployeeEntity entity);

}
