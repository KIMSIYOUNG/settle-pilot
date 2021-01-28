package in.woowa.pilot.batch.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import in.woowa.pilot.core.common.Status;
import in.woowa.pilot.core.order.Order;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.settle.SettleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static in.woowa.pilot.core.order.QOrder.order;
import static in.woowa.pilot.core.order.QOrderDetail.orderDetail;
import static in.woowa.pilot.core.owner.QOwner.owner;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Repository
public class OrderCustomRepository {
    private final JPAQueryFactory queryFactory;

    public List<Order> findUnSettledOrderByOwner(Owner target, SettleType type, LocalDate criteriaDate) {
        return queryFactory.select(order).distinct()
                .from(order)
                .innerJoin(order.owner, owner).fetchJoin()
                .innerJoin(order.orderDetails.orderDetails, orderDetail).fetchJoin()
                .where(
                        order.owner.eq(target),
                        order.owner.settleType.eq(type),
                        order.settle.isNull(),
                        order.status.eq(Status.ACTIVE),
                        owner.status.eq(Status.ACTIVE),
                        order.orderDateTime.goe(type.getStartCriteriaAt(criteriaDate)),
                        order.orderDateTime.lt(type.getEndCriteriaAt(criteriaDate))
                ).fetch();
    }
}
