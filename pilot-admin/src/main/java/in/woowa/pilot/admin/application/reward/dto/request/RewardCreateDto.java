package in.woowa.pilot.admin.application.reward.dto.request;

import in.woowa.pilot.core.order.BusinessNo;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.reward.Reward;
import in.woowa.pilot.core.reward.RewardType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class RewardCreateDto {
    private final BigDecimal amount;
    private final RewardType rewardType;
    private final String description;
    private final Long ownerId;
    private final LocalDateTime rewardDateTime;

    @Builder
    public RewardCreateDto(
            BigDecimal amount,
            RewardType rewardType,
            String description,
            Long ownerId,
            LocalDateTime rewardDateTime
    ) {
        this.amount = amount;
        this.rewardType = rewardType;
        this.description = description;
        this.ownerId = ownerId;
        this.rewardDateTime = rewardDateTime;
    }

    public Reward toReward(Owner owner) {
        return Reward.builder()
                .businessNo(new BusinessNo(LocalDate.now(), UUID.randomUUID().toString()))
                .amount(amount)
                .rewardType(rewardType)
                .description(description)
                .owner(owner)
                .rewardDateTime(rewardDateTime)
                .build();
    }
}
