package in.woowa.pilot.core.reward;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RewardRepository extends JpaRepository<Reward, Long> {

    @Transactional(readOnly = true)
    @Override
    @Query("select r " +
            "from Reward r " +
            "where r.id = :id " +
            "and r.status = 'ACTIVE'")
    Optional<Reward> findById(@Param("id") Long id);

    @Transactional(readOnly = true)
    @Override
    @Query("select r " +
            "from Reward r " +
            "where r.status = 'ACTIVE'")
    List<Reward> findAll();

    @Transactional(readOnly = true)
    @Query("select r " +
            "from Reward r " +
            "where r.status = 'ACTIVE'" +
            "and r.settle.id = :settleId")
    Page<Reward> findAllBySettleId(@Param("settleId") Long settleId, Pageable pageable);
}

