package in.woowa.pilot.core.owner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

    @Transactional(readOnly = true)
    @Override
    @Query("select o " +
            "from Owner o " +
            "where o.id = :id " +
            "and o.status = 'ACTIVE'")
    Optional<Owner> findById(@Param("id") Long id);

    @Transactional(readOnly = true)
    @Override
    @Query("select o " +
            "from Owner o " +
            "where o.status = 'ACTIVE'")
    List<Owner> findAll();
}
