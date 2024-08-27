package groupbee.employee.repository;

import groupbee.employee.entity.EmployeeEntity;
import groupbee.employee.repository.querydsl.employee.EmployeeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer>, EmployeeRepositoryCustom {

    long countById(String id);

    EmployeeEntity findByPotalId(String potalId);

    void deleteByPotalId(String id);
}
