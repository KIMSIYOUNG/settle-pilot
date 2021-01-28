package in.woowa.pilot.admin.repository.reward;

import in.woowa.pilot.admin.application.reward.dto.request.RewardSearchDto;
import in.woowa.pilot.core.reward.Reward;
import in.woowa.pilot.core.settle.Settle;
import in.woowa.pilot.core.settle.SettleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RewardCustomRepository {

    Optional<Reward> fetchById(Long id);

    Page<Reward> fetchPagedByCondition(RewardSearchDto condition, Pageable pageable);

    Page<Reward> fetchAll(Pageable pageable);

    long updateBatchSettle(List<Reward> rewards, Settle settle);

    List<Reward> fetchUnSettledByPeriod(RewardSearchDto condition);

    Map<Long, List<Reward>> fetchIdToRewardsBy(SettleType settleType, LocalDate criteriaDate);
}
