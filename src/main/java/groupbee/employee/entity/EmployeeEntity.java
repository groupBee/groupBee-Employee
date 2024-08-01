package groupbee.employee.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employee")
public class EmployeeEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "company_name", length = Integer.MAX_VALUE)
    private String companyName;

    @NotNull
    @Column(name = "potal_id", nullable = false, length = Integer.MAX_VALUE)
    private String potalId;

    @ColumnDefault("'password'")
    @Column(name = "passwd", length = Integer.MAX_VALUE)
    private String passwd;

    @NotNull
    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @Column(name = "resident_registration_number", length = Integer.MAX_VALUE)
    private String residentRegistrationNumber;

    @NotNull
    @Column(name = "\"position\"", nullable = false, length = Integer.MAX_VALUE)
    private String position;

    @Column(name = "email", length = Integer.MAX_VALUE)
    private String email;

    @Column(name = "extension_call", length = Integer.MAX_VALUE)
    private String extensionCall;

    @Column(name = "phone_number", length = Integer.MAX_VALUE)
    private String phoneNumber;

    @Column(name = "address", length = Integer.MAX_VALUE)
    private String address;

    @Column(name = "membership_status")
    private Boolean membershipStatus;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "department_id", nullable = false)
    private DepartmentEntity department;

    @Column(name = "profile_file", length = Integer.MAX_VALUE)
    private String profileFile;

}