package groupbee.employee.repository.querydsl.email;

import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepositoryCustom {
    void updateByPasswd(String passwd , String email);
}
