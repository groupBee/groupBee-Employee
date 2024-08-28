package groupbee.employee.mapper;

import groupbee.employee.dto.email.EmailDto;
import groupbee.employee.entity.EmailEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmailMapper {

    EmailDto toDto(EmailEntity entity);
}
