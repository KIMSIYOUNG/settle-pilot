package in.woowa.pilot.admin.application.reward.dto.request;

import in.woowa.pilot.core.order.BusinessNo;
import in.woowa.pilot.core.order.Order;
import in.woowa.pilot.core.reward.Reward;
import in.woowa.pilot.core.reward.RewardType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class RewardOrdersCreateDto {
    private final List<Long> orderIds;
    private final RewardType rewardType;
    private final String description;
    private final LocalDateTime rewardDateTime;

    @Builder
    public RewardOrdersCreateDto(List<Long> orderIds, RewardType rewardType, String description, LocalDateTime rewardDateTime) {
        this.orderIds = orderIds;
        this.rewardType = rewardType;
        this.description = description;
        this.rewardDateTime = rewardDateTime;
    }

    public Reward toReward(Order order) {
        return Reward.builder()
                .businessNo(new BusinessNo(LocalDate.now(), UUID.randomUUID().toString()))
                .amount(order.getAmount())
                .order(order)
                .rewardType(rewardType)
                .rewardDateTime(rewardDateTime)
                .owner(order.getOwner())
                .description(description)
                .build();
    }
}
