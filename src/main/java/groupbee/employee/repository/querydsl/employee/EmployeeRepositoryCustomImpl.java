package groupbee.employee.repository.querydsl.employee;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import groupbee.employee.entity.EmployeeEntity;
import groupbee.employee.entity.QEmployeeEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmployeeRepositoryCustomImpl implements EmployeeRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QEmployeeEntity employee = QEmployeeEntity.employeeEntity;

    @Transactional
    public void updateByPasswd(String passwd, String potalId) {
        jpaQueryFactory.update(employee)
                .set(employee.passwd, passwd)
                .where(employee.potalId.eq(potalId))
                .execute();
    }

    @Transactional
    public void updateWithoutPasswd(EmployeeEntity employeeEntity) {
        jpaQueryFactory.update(employee)
                .set(employee.email, employeeEntity.getEmail())
                .set(employee.extensionCall, employeeEntity.getExtensionCall())
                .set(employee.phoneNumber, employeeEntity.getPhoneNumber())
                .set(employee.address, employeeEntity.getAddress())
                .set(employee.position, employeeEntity.getPosition())
                .set(employee.department, employeeEntity.getDepartment())
                .set(employee.name, employeeEntity.getName())
                .where(employee.id.eq(employeeEntity.getId()))
                .execute();
    }

    @Override
    @Transactional
    public void updateAll(EmployeeEntity entity) {
    }

    @Override
    public EmployeeEntity findListByPotalId(String potalId) {
        return jpaQueryFactory.select(Projections.fields(
                        EmployeeEntity.class,
                        employee.id,
                        employee.potalId,
                        employee.name,
                        employee.residentRegistrationNumber,
                        employee.position,
                        employee.email,
                        employee.extensionCall,
                        employee.phoneNumber,
                        employee.address,
                        employee.membershipStatus,
                        employee.department,
                        employee.profileFile,
                        employee.companyName,
                        employee.isAdmin
                ))
                .from(employee)
                .where(employee.potalId.eq(potalId))
                .fetch().getFirst();

    }
}
