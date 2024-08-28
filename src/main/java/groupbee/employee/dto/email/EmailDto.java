package groupbee.employee.dto.email;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDto {

  private String email;
  private String password;

}
