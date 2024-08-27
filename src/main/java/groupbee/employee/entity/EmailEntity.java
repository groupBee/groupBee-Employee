package groupbee.employee.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Entity
@Table(name = EmailEntity.TABLE_NAME, schema = "public")
public class EmailEntity {
    public static final String TABLE_NAME = "email";
    public static final String COLUMN_EMAIL_NAME = "email";
    public static final String COLUMN_PASSWORD_NAME = "password";

    private String email;

    private String password;

    @Id
    @Column(name = COLUMN_EMAIL_NAME, nullable = false, length = Integer.MAX_VALUE)
    public String getEmail() {
        return email;
    }

    @Column(name = COLUMN_PASSWORD_NAME, length = Integer.MAX_VALUE)
    public String getPassword() {
        return password;
    }

}