package in.woowa.pilot.batch.processor;

import in.woowa.pilot.batch.dto.SettleJobDto;
import in.woowa.pilot.batch.parameter.SettleParameter;
import in.woowa.pilot.batch.repository.OrderCustomRepository;
import in.woowa.pilot.batch.repository.RewardCustomRepository;
import in.woowa.pilot.core.order.Order;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.reward.Reward;
import in.woowa.pilot.core.settle.SettleType;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SettleJobProcessor implements ItemProcessor<Owner, SettleJobDto> {
    private final RewardCustomRepository rewardCustomRepository;
    private final OrderCustomRepository orderCustomRepository;
    private final SettleParameter jobParameter;

    @Override
    public SettleJobDto process(Owner owner) throws Exception {
        LocalDate criteriaDate = jobParameter.getCriteriaDate();
        SettleType type = jobParameter.getSettleType();

        List<Reward> rewards = rewardCustomRepository.findUnSettledRewardByOwner(owner, type, criteriaDate);
        List<Order> orders = orderCustomRepository.findUnSettledOrderByOwner(owner, type, criteriaDate);

        return new SettleJobDto(owner, rewards, orders);
    }
}
