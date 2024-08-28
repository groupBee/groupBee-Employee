package groupbee.employee.mapper;


import groupbee.employee.dto.employee.EmployeeDto;
import groupbee.employee.entity.EmployeeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(source = "departmentId", target = "department.id")
    @Mapping(source = "position", target = "position.id")
    EmployeeEntity toEntity(EmployeeDto dto);

    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "position.id", target = "position")
    EmployeeDto toDto(EmployeeEntity entity);

}
