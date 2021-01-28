package in.woowa.pilot.admin.application.reward.dto.request;

import in.woowa.pilot.core.order.BusinessNo;
import in.woowa.pilot.core.order.Order;
import in.woowa.pilot.core.reward.Reward;
import in.woowa.pilot.core.reward.RewardType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class RewardPeriodDto {
    private final RewardType rewardType;
    private final String description;
    private final LocalDateTime rewardDateTime;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;

    @Builder
    public RewardPeriodDto(
            RewardType rewardType,
            String description,
            LocalDateTime rewardDateTime,
            LocalDateTime startAt,
            LocalDateTime endAt
    ) {
        this.rewardType = rewardType;
        this.description = description;
        this.rewardDateTime = rewardDateTime;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public Reward toReward(Order order) {
        return Reward.builder()
                .businessNo(new BusinessNo(LocalDate.now(), UUID.randomUUID().toString()))
                .rewardDateTime(rewardDateTime)
                .rewardType(rewardType)
                .description(description)
                .amount(order.getAmount())
                .owner(order.getOwner())
                .order(order)
                .build();
    }
}
