package groupbee.employee.repository;

import groupbee.employee.entity.RankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankRepository extends JpaRepository<RankEntity, Long> {
}
