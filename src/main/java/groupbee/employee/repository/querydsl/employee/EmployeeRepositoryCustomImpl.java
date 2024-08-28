package groupbee.employee.repository.querydsl.employee;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import groupbee.employee.dto.employee.EmployeeDetailDto;
import groupbee.employee.dto.employee.EmployeeListDto;
import groupbee.employee.entity.EmployeeEntity;
import groupbee.employee.entity.QEmployeeEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
        jpaQueryFactory.update(employee)
                .set(employee.id, entity.getId())
                .set(employee.potalId, entity.getPotalId())
                .set(employee.name, entity.getName())
                .set(employee.residentRegistrationNumber, entity.getResidentRegistrationNumber())
                .set(employee.position, entity.getPosition())
                .set(employee.email, entity.getEmail())
                .set(employee.phoneNumber, entity.getPhoneNumber())
                .set(employee.extensionCall, entity.getExtensionCall())
                .set(employee.address, entity.getAddress())
                .set(employee.membershipStatus, entity.getMembershipStatus())
                .set(employee.department, entity.getDepartment())
                .set(employee.profileFile, entity.getProfileFile())
                .set(employee.companyName, entity.getCompanyName())
                .set(employee.isAdmin, entity.getIsAdmin())
                .set(employee.idNumber, entity.getIdNumber())
                .set(employee.firstDay, entity.getFirstDay())
                .where(employee.id.eq(entity.getId()))
                .execute();
    }

    @Override
    @Transactional
    public List<EmployeeListDto> findListAll() {
        return jpaQueryFactory.select(Projections.fields(
                EmployeeListDto.class,
                employee.id,
                employee.name,
                employee.position,
                employee.email,
                employee.phoneNumber,
                employee.membershipStatus,
                employee.department,
                employee.extensionCall,
                employee.profileFile,
                employee.firstDay
        )).from(employee).fetch();
    }

    @Override
    public List<EmployeeListDto> findListByDepartmentId(Long departmentId) {
        return jpaQueryFactory.select(Projections.fields(
                EmployeeListDto.class,
                employee.id,
                employee.name,
                employee.position,
                employee.email,
                employee.phoneNumber,
                employee.membershipStatus,
                employee.department,
                employee.extensionCall,
                employee.profileFile,
                employee.firstDay
        ))
                .from(employee)
                .where(employee.department.id.eq(departmentId))
                .fetch();
    }

    @Override
    @Transactional
    public EmployeeDetailDto findDetailById(String id) {
        return jpaQueryFactory.select(
                Projections.fields(
                    EmployeeDetailDto.class,
                    employee.id,
                    employee.potalId,
                    employee.name,
                    employee.residentRegistrationNumber,
                    employee.position,
                    employee.email,
                    employee.phoneNumber,
                    employee.extensionCall,
                    employee.address,
                    employee.membershipStatus,
                    employee.department,
                    employee.profileFile,
                    employee.companyName,
                    employee.isAdmin,
                    employee.idNumber,
                    employee.firstDay
                    )
                )
                .from(employee)
                .where(employee.id.eq(id))
                .fetchFirst();
    }


}
