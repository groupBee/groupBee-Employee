package groupbee.employee.dto.employee;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

  private String id;
  private String potalId;
  private String passwd;
  private String name;
  private String residentRegistrationNumber;
  private Long position;
  private String email;
  private String extensionCall;
  private String phoneNumber;
  private String address;
  private Boolean membershipStatus;
  private Long departmentId;
  private String profileFile;
  private String companyName;
  private Boolean isAdmin;
  private String idNumber;
  private String firstDay;
}