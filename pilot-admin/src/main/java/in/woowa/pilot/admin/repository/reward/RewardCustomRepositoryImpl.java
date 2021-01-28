package in.woowa.pilot.admin.repository.reward;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import in.woowa.pilot.admin.application.reward.dto.request.RewardSearchDto;
import in.woowa.pilot.core.common.Status;
import in.woowa.pilot.core.reward.Reward;
import in.woowa.pilot.core.reward.RewardType;
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

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static in.woowa.pilot.core.order.QOrder.order;
import static in.woowa.pilot.core.owner.QOwner.owner;
import static in.woowa.pilot.core.reward.QReward.reward;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class RewardCustomRepositoryImpl implements RewardCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Reward> fetchById(Long id) {
        return Optional.ofNullable(queryFactory.select(reward)
                .from(reward)
                .innerJoin(reward.owner, owner).fetchJoin()
                .where(
                        isActiveReward(),
                        isActiveOwner(),
                        reward.id.eq(id)
                )
                .fetchOne());
    }

    @Override
    public Page<Reward> fetchPagedByCondition(RewardSearchDto condition, Pageable pageable) {
        List<Reward> results = queryFactory.select(reward)
                .from(reward)
                .leftJoin(reward.order, order).fetchJoin()
                .innerJoin(reward.owner, owner).fetchJoin()
                .where(
                        isActiveReward(),
                        isActiveOwner(),
                        isSameOwner(condition.getOwnerId()),
                        containName(condition.getOwnerName()),
                        containEmail(condition.getOwnerEmail()),
                        containRewardNo(condition.getRewardNo()),
                        isBetween(condition.getStartAt(), condition.getEndAt()),
                        isSameRewardType(condition.getRewardType())
                )
                .orderBy(reward.updatedAt.desc(), reward.rewardDateTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(results, pageable, () -> countReward(condition));
    }

    @Override
    public Page<Reward> fetchAll(Pageable pageable) {
        List<Reward> results = queryFactory.select(reward)
                .from(reward)
                .innerJoin(reward.owner, owner)
                .where(isActiveOwner(), isActiveReward())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(results, pageable, this::countReward);
    }

    @Override
    public List<Reward> fetchUnSettledByPeriod(RewardSearchDto condition) {
        return queryFactory.select(reward)
                .from(reward)
                .innerJoin(reward.owner, owner).fetchJoin()
                .where(
                        isActiveReward(),
                        isActiveOwner(),
                        isSameOwner(condition.getOwnerId()),
                        reward.settle.isNull(),
                        reward.rewardDateTime.goe(condition.getStartAt()),
                        reward.rewardDateTime.lt(condition.getEndAt())
                ).fetch();
    }

    @Override
    public Map<Long, List<Reward>> fetchIdToRewardsBy(SettleType settleType, LocalDate criteriaDate) {
        return queryFactory.select(reward).distinct()
                .from(reward)
                .innerJoin(reward.owner, owner).fetchJoin()
                .where(
                        isActiveOwner(),
                        isActiveReward(),
                        reward.settle.isNull(),
                        reward.owner.settleType.eq(settleType),
                        reward.rewardDateTime.goe(settleType.getStartCriteriaAt(criteriaDate)),
                        reward.rewardDateTime.lt(settleType.getEndCriteriaAt(criteriaDate))
                )
                .transform(groupBy(owner.id).as(list(reward)));
    }

    @Override
    @Transactional
    public long updateBatchSettle(List<Reward> rewards, Settle settle) {
        return queryFactory.update(reward)
                .set(reward.settle, settle)
                .where(reward.in(rewards), isActiveReward())
                .execute();
    }

    private long countReward(RewardSearchDto condition) {
        return queryFactory.select(reward)
                .from(reward)
                .innerJoin(reward.owner, owner).fetchJoin()
                .where(
                        isActiveReward(),
                        isActiveOwner(),
                        isSameOwner(condition.getOwnerId()),
                        containName(condition.getOwnerName()),
                        containEmail(condition.getOwnerEmail()),
                        isBetween(condition.getStartAt(), condition.getEndAt()),
                        isSameRewardType(condition.getRewardType()),
                        containRewardNo(condition.getRewardNo())
                )
                .fetchCount();
    }

    private long countReward() {
        return queryFactory.select(reward)
                .from(reward)
                .innerJoin(reward.owner, owner).fetchJoin()
                .where(isActiveOwner(), isActiveReward())
                .fetchCount();
    }

    private BooleanExpression isSameOwner(Long ownerId) {
        return ownerId == null ? null : owner.id.eq(ownerId);
    }

    private BooleanExpression containName(String name) {
        return StringUtils.hasText(name) ? reward.owner.name.contains(name) : null;
    }

    private BooleanExpression containRewardNo(String rewardNo) {
        return StringUtils.hasText(rewardNo) ? reward.businessNo.businessNo.contains(rewardNo) : null;
    }

    private BooleanExpression containEmail(String email) {
        return StringUtils.hasText(email) ? reward.owner.email.contains(email) : null;
    }

    private Predicate isBetween(LocalDateTime startAt, LocalDateTime endAt) {
        return (startAt == null || endAt == null) ? null : reward.rewardDateTime.between(startAt, endAt);
    }

    private BooleanExpression isSameRewardType(RewardType rewardType) {
        return rewardType == null ? null : reward.rewardType.eq(rewardType);
    }

    private BooleanExpression isActiveOwner() {
        return owner.status.eq(Status.ACTIVE);
    }

    private BooleanExpression isActiveReward() {
        return reward.status.eq(Status.ACTIVE);
    }
}
