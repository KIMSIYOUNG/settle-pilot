package in.woowa.pilot.core.settle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SettleSnapshotRepository extends JpaRepository<SettleSnapshot, Long> {

    @Query("select s from SettleSnapshot s where s.startAt >= :start and s.endAt <= :end")
    List<SettleSnapshot> findByPeriod(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
