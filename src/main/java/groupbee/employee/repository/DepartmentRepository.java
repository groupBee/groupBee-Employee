package groupbee.employee.repository;

import groupbee.employee.entity.DepartmentEntity;
import groupbee.employee.repository.querydsl.EmployeeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity , Integer>  {
}
