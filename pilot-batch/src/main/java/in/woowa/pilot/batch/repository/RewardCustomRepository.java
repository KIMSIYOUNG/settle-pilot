package in.woowa.pilot.batch.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import in.woowa.pilot.core.common.Status;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.reward.Reward;
import in.woowa.pilot.core.settle.SettleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static in.woowa.pilot.core.owner.QOwner.owner;
import static in.woowa.pilot.core.reward.QReward.reward;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Repository
public class RewardCustomRepository {
    private final JPAQueryFactory queryFactory;

    public List<Reward> findUnSettledRewardByOwner(Owner target, SettleType type, LocalDate criteriaDate) {
        return queryFactory.select(reward)
                .from(reward)
                .innerJoin(reward.owner, owner).fetchJoin()
                .where(
                        reward.owner.eq(target),
                        reward.owner.settleType.eq(type),
                        reward.settle.isNull(),
                        reward.status.eq(Status.ACTIVE),
                        owner.status.eq(Status.ACTIVE),
                        reward.rewardDateTime.goe(type.getStartCriteriaAt(criteriaDate)),
                        reward.rewardDateTime.lt(type.getEndCriteriaAt(criteriaDate))
                ).fetch();
    }
}
