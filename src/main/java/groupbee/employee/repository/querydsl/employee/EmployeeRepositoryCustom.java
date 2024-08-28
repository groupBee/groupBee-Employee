package groupbee.employee.repository.querydsl.employee;

import groupbee.employee.dto.employee.EmployeeDetailDto;
import groupbee.employee.dto.employee.EmployeeListDto;
import groupbee.employee.entity.EmployeeEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepositoryCustom {
    void updateByPasswd(String passwd,String potalId);

    void updateWithoutPasswd(EmployeeEntity entity);

    void updateAll(EmployeeEntity entity);

    List<EmployeeListDto> findListAll();

    List<EmployeeListDto> findListByDepartmentId(Long departmentId);

    EmployeeDetailDto findDetailById(String id);
}
