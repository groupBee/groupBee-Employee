package groupbee.employee.dto.rank;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RankDto {
    private Long id;
    private String rank;
}
