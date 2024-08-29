package groupbee.employee.mapper;

import groupbee.employee.dto.rank.RankDto;
import groupbee.employee.entity.RankEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RankMapper {

    RankDto toDto(RankEntity entity);

    RankEntity toEntity(RankDto dto);

    List<RankDto> toDtos(List<RankEntity> entities);
}
