package groupbee.employee.repository.querydsl.email;

import com.querydsl.jpa.impl.JPAQueryFactory;
import groupbee.employee.entity.QEmailEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailRepositoryCustomImpl implements EmailRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private QEmailEntity emailEntity = QEmailEntity.emailEntity;

    @Override
    @Transactional
    public void updateByPasswd(String passwd, String email) {
        jpaQueryFactory.update(emailEntity)
                .set(emailEntity.password, passwd)
                .where(emailEntity.email.eq(email))
                .execute();
    }
}
