package groupbee.employee.repository;

import groupbee.employee.dto.EmailDto;
import groupbee.employee.entity.EmailEntity;
import groupbee.employee.repository.querydsl.email.EmailRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<EmailEntity, String>, EmailRepositoryCustom {
    EmailEntity findByEmail(String email);
}
