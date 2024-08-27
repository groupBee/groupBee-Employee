package groupbee.employee.mapper;

import groupbee.employee.dto.EmailDto;
import groupbee.employee.entity.EmailEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmailMapper {

    EmailDto toDto(EmailEntity entity);
}
