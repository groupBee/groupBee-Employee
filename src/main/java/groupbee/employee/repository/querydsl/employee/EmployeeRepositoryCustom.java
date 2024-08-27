package groupbee.employee.repository.querydsl.employee;

import groupbee.employee.entity.EmployeeEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepositoryCustom {
    void updateByPasswd(String passwd,String potalId);

    void updateWithoutPasswd(EmployeeEntity entity);
}
