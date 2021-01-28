package in.woowa.pilot.core.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Transactional(readOnly = true)
    @Override
    @Query("select o " +
            "from Order o " +
            "where o.id = :id " +
            "and o.status = 'ACTIVE'")
    Optional<Order> findById(@Param("id") Long id);

    @Transactional(readOnly = true)
    @Override
    @Query("select o " +
            "from Order o " +
            "where o.status = 'ACTIVE'")
    List<Order> findAll();
}
