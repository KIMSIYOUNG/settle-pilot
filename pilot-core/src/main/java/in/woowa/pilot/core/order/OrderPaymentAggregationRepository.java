package in.woowa.pilot.core.order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderPaymentAggregationRepository extends JpaRepository<OrderPaymentAggregation, Long> {
}
