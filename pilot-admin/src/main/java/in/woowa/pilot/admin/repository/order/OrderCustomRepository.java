package in.woowa.pilot.admin.repository.order;

import in.woowa.pilot.admin.application.order.dto.request.OrderSearchDto;
import in.woowa.pilot.core.order.Order;
import in.woowa.pilot.core.settle.Settle;
import in.woowa.pilot.core.settle.SettleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderCustomRepository {

    Optional<Order> fetchById(Long id);

    Optional<Order> findByOrderNo(String orderNo);

    Page<Order> fetchPagedByCondition(OrderSearchDto condition, Pageable pageable);

    List<Order> fetchByPeriod(LocalDateTime startAt, LocalDateTime endAt);

    Page<Order> findAllBySettleId(Long id, Pageable pageable);

    Optional<Order> fetchAggregateById(Long id);

    long updateBatchSettle(List<Order> orders, Settle settle);

    List<Order> fetchUnSettledByPeriod(OrderSearchDto condition);

    Map<Long, List<Order>> fetchIdToOrdersBy(SettleType settleType, LocalDate criteriaDate);
}
