package in.woowa.pilot.batch.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import in.woowa.pilot.core.common.Status;
import in.woowa.pilot.core.order.Order;
import in.woowa.pilot.core.settle.Settle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static in.woowa.pilot.core.order.QOrder.order;

@RequiredArgsConstructor
@Repository
public class OrderBatchRepository {
    private final JPAQueryFactory queryFactory;

    @Transactional
    public long updateBatchSettle(List<Order> orders, Settle settle) {
        return queryFactory.update(order)
                .set(order.settle, settle)
                .where(order.in(orders), order.status.eq(Status.ACTIVE))
                .execute();
    }

}
