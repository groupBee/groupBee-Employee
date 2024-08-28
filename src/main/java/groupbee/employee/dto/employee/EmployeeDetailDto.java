package groupbee.employee.dto.employee;

import groupbee.employee.entity.DepartmentEntity;
import groupbee.employee.entity.RankEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDetailDto {
    private String id;
    private String potalId;
    private String name;
    private String residentRegistrationNumber;
    private RankEntity position;
    private String email;
    private String phoneNumber;
    private String extensionCall;
    private String address;
    private Boolean membershipStatus;
    private DepartmentEntity department;
    private String profileFile;
    private String companyName;
    private Boolean isAdmin;
    private String idNumber;
    private String firstDay;
}
