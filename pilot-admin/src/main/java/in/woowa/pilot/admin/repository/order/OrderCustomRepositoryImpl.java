package in.woowa.pilot.admin.repository.order;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import in.woowa.pilot.admin.application.order.dto.request.OrderSearchDto;
import in.woowa.pilot.core.common.Status;
import in.woowa.pilot.core.order.Order;
import in.woowa.pilot.core.order.OrderStatus;
import in.woowa.pilot.core.settle.Settle;
import in.woowa.pilot.core.settle.SettleType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static in.woowa.pilot.core.order.QOrder.order;
import static in.woowa.pilot.core.order.QOrderDetail.orderDetail;
import static in.woowa.pilot.core.order.QOrderSnapShot.orderSnapShot;
import static in.woowa.pilot.core.owner.QOwner.owner;
import static in.woowa.pilot.core.settle.QSettle.settle;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class OrderCustomRepositoryImpl implements OrderCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Order> fetchById(Long id) {
        return Optional.ofNullable(queryFactory.select(order)
                .from(order)
                .innerJoin(order.owner, owner).fetchJoin()
                .where(isSameOrder(id), isActiveOwner(), isActiveOrder())
                .fetchOne());
    }

    @Override
    public Optional<Order> findByOrderNo(String orderNo) {
        return Optional.ofNullable(queryFactory.select(order)
                .from(order)
                .innerJoin(order.owner, owner).fetchJoin()
                .where(containOrderNo(orderNo), isActiveOrder())
                .fetchOne());
    }

    @Override
    public Page<Order> fetchPagedByCondition(OrderSearchDto condition, Pageable pageable) {
        List<Order> content = queryFactory.select(order)
                .from(order)
                .innerJoin(order.owner, owner).fetchJoin()
                .where(
                        isActiveOwner(),
                        isActiveOrder(),
                        isSameOwner(condition.getOwnerId()),
                        containOrderNo(condition.getOrderNo()),
                        isBetween(order.orderDateTime, condition.getStartAt(), condition.getEndAt()),
                        isSameOrderStatus(condition.getOrderStatus())
                )
                .orderBy(order.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, () -> countOrder(condition));
    }

    @Override
    public List<Order> fetchByPeriod(LocalDateTime startAt, LocalDateTime endAt) {
        return queryFactory.select(order).distinct()
                .from(order)
                .innerJoin(order.orderDetails.orderDetails, orderDetail).fetchJoin()
                .where(isBetween(order.orderDateTime, startAt, endAt), isActiveOrder())
                .fetch();
    }

    @Override
    public Page<Order> findAllBySettleId(Long id, Pageable pageable) {
        List<Order> content = queryFactory.select(order)
                .from(order)
                .innerJoin(order.owner, owner).fetchJoin()
                .innerJoin(order.settle, settle)
                .where(
                        isActiveOwner(),
                        isActiveOrder(),
                        order.settle.id.eq(id)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, () -> countOrder(id));
    }

    @Override
    public Optional<Order> fetchAggregateById(Long id) {
        return Optional.ofNullable(queryFactory.select(order).distinct()
                .from(order)
                .innerJoin(order.orderDetails.orderDetails, orderDetail).fetchJoin()
                .where(isSameOrder(id), isActiveOrder())
                .fetchOne());
    }

    @Override
    @Transactional
    public long updateBatchSettle(List<Order> orders, Settle settle) {
        return queryFactory.update(order)
                .set(order.settle, settle)
                .where(order.in(orders), isActiveOrder())
                .execute();
    }

    @Override
    public List<Order> fetchUnSettledByPeriod(OrderSearchDto condition) {
        return queryFactory.select(order).distinct()
                .from(order)
                .innerJoin(order.owner, owner).fetchJoin()
                .innerJoin(order.orderSnapShots, orderSnapShot).fetchJoin()
                .where(
                        isActiveOwner(),
                        isActiveOrder(),
                        orderSnapShot.orderStatus.eq(OrderStatus.DELIVERY_CONFIRM),
                        orderSnapShot.statusAt.goe(condition.getStartAt()),
                        orderSnapShot.statusAt.lt(condition.getEndAt()),
                        order.settle.isNull(),
                        isSameOwner(condition.getOwnerId())
                )
                .fetch();
    }

    @Override
    public Map<Long, List<Order>> fetchIdToOrdersBy(SettleType settleType, LocalDate criteriaDate) {
        return queryFactory.select(order).distinct()
                .from(order)
                .innerJoin(order.owner, owner).fetchJoin()
                .innerJoin(order.orderSnapShots, orderSnapShot)
                .where(
                        isActiveOwner(),
                        isActiveOrder(),
                        order.settle.isNull(),
                        order.owner.settleType.eq(settleType),
                        orderSnapShot.orderStatus.eq(OrderStatus.DELIVERY_CONFIRM),
                        orderSnapShot.statusAt.goe(settleType.getStartCriteriaAt(criteriaDate)),
                        orderSnapShot.statusAt.lt(settleType.getEndCriteriaAt(criteriaDate))
                )
                .transform(GroupBy.groupBy(owner.id).as(GroupBy.list(order)));
    }

    private Long countOrder(OrderSearchDto condition) {
        return queryFactory.select(order)
                .from(order)
                .innerJoin(order.owner, owner)
                .where(
                        isActiveOwner(),
                        isActiveOrder(),
                        isSameOwner(condition.getOwnerId()),
                        containOrderNo(condition.getOrderNo()),
                        isBetween(order.orderDateTime, condition.getStartAt(), condition.getEndAt()),
                        isSameOrderStatus(condition.getOrderStatus())
                )
                .fetchCount();
    }

    private Long countOrder(Long settleId) {
        return queryFactory.select(order)
                .from(order)
                .innerJoin(order.owner, owner)
                .where(
                        isActiveOwner(),
                        isActiveOrder(),
                        order.settle.id.eq(settleId)
                )
                .fetchCount();
    }

    private BooleanExpression isActiveOwner() {
        return order.owner.status.eq(Status.ACTIVE);
    }

    private BooleanExpression containOrderNo(String orderNo) {
        return StringUtils.hasText(orderNo) ? order.businessNo.businessNo.contains(orderNo) : null;
    }

    private BooleanExpression isSameOrder(Long orderId) {
        return orderId == null ? null : order.id.eq(orderId);
    }

    private BooleanExpression isSameOwner(Long ownerId) {
        return ownerId == null ? null : order.owner.id.eq(ownerId);
    }

    private BooleanExpression isSameOrderStatus(OrderStatus orderStatus) {
        return orderStatus == null ? null : order.orderStatus.eq(orderStatus);
    }

    private BooleanExpression isBetween(DateTimePath<LocalDateTime> target, LocalDateTime startAt, LocalDateTime endAt) {
        return (startAt == null || endAt == null) ? null : target.between(startAt, endAt);
    }

    private BooleanExpression isActiveOrder() {
        return order.status.eq(Status.ACTIVE);
    }
}
