package in.woowa.pilot.core.settle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SettleRepository extends JpaRepository<Settle, Long> {

    @Transactional(readOnly = true)
    @Override
    @Query("select s " +
            "from Settle s " +
            "where s.id = :id " +
            "and s.status = 'ACTIVE'")
    Optional<Settle> findById(@Param("id") Long id);

    @Transactional(readOnly = true)
    @Override
    @Query("select s " +
            "from Settle s " +
            "where s.status = 'ACTIVE'")
    List<Settle> findAll();
}
