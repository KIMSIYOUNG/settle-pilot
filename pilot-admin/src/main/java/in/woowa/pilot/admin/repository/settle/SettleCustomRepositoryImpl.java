package in.woowa.pilot.admin.repository.settle;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import in.woowa.pilot.admin.application.settle.dto.request.SettleCompleteConditionDto;
import in.woowa.pilot.admin.application.settle.dto.request.SettleSearchDto;
import in.woowa.pilot.core.common.Status;
import in.woowa.pilot.core.settle.Settle;
import in.woowa.pilot.core.settle.SettleStatus;
import in.woowa.pilot.core.settle.SettleType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

import static in.woowa.pilot.core.owner.QOwner.owner;
import static in.woowa.pilot.core.settle.QSettle.settle;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class SettleCustomRepositoryImpl implements SettleCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Settle> fetchPagedByCondition(SettleSearchDto condition, Pageable pageable) {
        List<Settle> content = queryFactory.select(settle)
                .from(settle)
                .innerJoin(settle.owner, owner).fetchJoin()
                .where(
                        isActiveOwner(),
                        isActiveSettle(),
                        isSameOwner(condition.getOwnerId()),
                        isAfter(condition.getStartAt()),
                        isBefore(condition.getEndAt()),
                        isSameSettleStatus(condition.getSettleStatus()),
                        isSameSettleType(condition.getSettleType())
                )
                .orderBy(settle.updatedAt.desc(), settle.createdAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery(condition));
    }

    @Override
    public Page<Settle> fetchAll(Pageable pageable) {
        List<Settle> content = queryFactory.select(settle)
                .from(settle)
                .innerJoin(settle.owner, owner).fetchJoin()
                .where(
                        isActiveOwner(),
                        isActiveSettle()
                )
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, this::countQuery);
    }

    @Override
    public List<Settle> fetchByCondition(SettleSearchDto condition) {
        return queryFactory.select(settle)
                .from(settle)
                .innerJoin(settle.owner, owner).fetchJoin()
                .where(
                        isActiveOwner(),
                        isActiveSettle(),
                        isSameOwner(condition.getOwnerId()),
                        isBefore(condition.getEndAt()),
                        isAfter(condition.getStartAt()),
                        isSameSettleType(condition.getSettleType()),
                        isSameSettleStatus(condition.getSettleStatus())
                ).fetch();
    }

    @Override
    @Transactional
    public long updateBulkComplete(SettleCompleteConditionDto condition) {
        return queryFactory.update(settle)
                .set(settle.settleStatus, SettleStatus.COMPLETED)
                .set(settle.settleCompleteDate, LocalDateTime.now())
                .where(
                        isActiveSettle(),
                        isSameOwner(condition.getOwnerId()),
                        isBefore(condition.getEndAt()),
                        isAfter(condition.getStartAt()),
                        isInIds(condition.getSettleIds())
                ).execute();
    }

    private Long countQuery(SettleSearchDto condition) {
        return queryFactory.select(settle)
                .from(settle)
                .innerJoin(settle.owner, owner).fetchJoin()
                .where(
                        isActiveOwner(),
                        isActiveSettle(),
                        isSameOwner(condition.getOwnerId()),
                        isAfter(condition.getStartAt()),
                        isBefore(condition.getEndAt()),
                        isSameSettleStatus(condition.getSettleStatus()),
                        isSameSettleType(condition.getSettleType())
                ).fetchCount();
    }

    private Long countQuery() {
        return queryFactory.select(settle)
                .from(settle)
                .innerJoin(settle.owner, owner).fetchJoin()
                .where(
                        isActiveOwner(),
                        isActiveSettle()
                ).fetchCount();
    }

    private BooleanExpression isSameSettleStatus(SettleStatus status) {
        return status == null ? null : settle.settleStatus.eq(status);
    }

    private BooleanExpression isInIds(List<Long> settleIds) {
        return CollectionUtils.isEmpty(settleIds) ? null : settle.id.in(settleIds);
    }

    private BooleanExpression isBefore(LocalDateTime endAt) {
        return endAt == null ? null : settle.transactionEndAt.loe(endAt);
    }

    private BooleanExpression isAfter(LocalDateTime startAt) {
        return startAt == null ? null : settle.transactionStartAt.goe(startAt);
    }

    private BooleanExpression isSameOwner(Long ownerId) {
        return ownerId == null ? null : settle.owner.id.eq(ownerId);
    }

    private BooleanExpression isSameSettleType(SettleType settleType) {
        return settleType == null ? null : settle.settleType.eq(settleType);
    }

    private BooleanExpression isActiveOwner() {
        return settle.owner.status.eq(Status.ACTIVE);
    }

    private BooleanExpression isActiveSettle() {
        return settle.status.eq(Status.ACTIVE);
    }
}
