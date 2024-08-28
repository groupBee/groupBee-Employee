package groupbee.employee.dto.employee;

import groupbee.employee.entity.DepartmentEntity;
import groupbee.employee.entity.RankEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeListDto {
    private String id;
    private String name;
    private RankEntity position;
    private String email;
    private String phoneNumber;
    private Boolean membershipStatus;
    private DepartmentEntity departmentName;
    private String extensionCall;
    private String profileFile;
    private String firstDay;
}
