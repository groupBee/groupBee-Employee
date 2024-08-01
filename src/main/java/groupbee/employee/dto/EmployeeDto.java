package groupbee.employee.dto;

import groupbee.employee.entity.EmployeeEntity;
import groupbee.employee.service.employee.DepartmentService;
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
  private String position;
  private String email;
  private String extensionCall;
  private String phoneNumber;
  private String address;
  private Boolean membershipStatus;
  private Long departmentId;
  private String profileFile;
  private String companyName;

}