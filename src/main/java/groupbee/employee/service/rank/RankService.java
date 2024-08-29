package groupbee.employee.service.rank;

import groupbee.employee.dto.rank.RankDto;
import groupbee.employee.mapper.RankMapper;
import groupbee.employee.repository.RankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankService {
    private final RankRepository rankRepository;
    private final RankMapper rankMapper;

    public List<RankDto> getRanks() {
        return rankMapper.toDtos(rankRepository.findAll());
    }
}
