package groupbee.employee.mapper;

import groupbee.employee.dto.DepartmentDto;
import groupbee.employee.entity.DepartmentEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    DepartmentDto toDto(DepartmentEntity entity);
    List<DepartmentDto> listDto (List<DepartmentEntity> entity);
}
