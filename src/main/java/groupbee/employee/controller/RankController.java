package groupbee.employee.controller;

import groupbee.employee.dto.rank.RankDto;
import groupbee.employee.service.rank.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RankController {
    private final RankService rankService;

    @GetMapping("/rank/all")
    public List<RankDto> getRanks() {
        return rankService.getRanks();
    }
}
