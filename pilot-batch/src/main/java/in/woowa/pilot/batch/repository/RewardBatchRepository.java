package in.woowa.pilot.batch.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import in.woowa.pilot.core.common.Status;
import in.woowa.pilot.core.reward.Reward;
import in.woowa.pilot.core.settle.Settle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static in.woowa.pilot.core.reward.QReward.reward;

@RequiredArgsConstructor
@Repository
public class RewardBatchRepository {
    private final JPAQueryFactory queryFactory;

    @Transactional
    public long updateBatchSettle(List<Reward> rewards, Settle settle) {
        return queryFactory.update(reward)
                .set(reward.settle, settle)
                .where(reward.in(rewards), reward.status.eq(Status.ACTIVE))
                .execute();
    }

}
