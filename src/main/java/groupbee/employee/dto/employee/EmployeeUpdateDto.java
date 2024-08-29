package groupbee.employee.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateDto {
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
    private MultipartFile file;
}
