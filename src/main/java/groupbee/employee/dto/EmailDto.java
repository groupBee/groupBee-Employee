package groupbee.employee.dto;

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
