package in.woowa.pilot.admin.application.reward.dto.request;

import in.woowa.pilot.core.reward.RewardType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class RewardUpdateDto {
    private final Long id;
    private final BigDecimal amount;
    private final RewardType rewardType;
    private final String description;

    @Builder
    public RewardUpdateDto(Long id, BigDecimal amount, RewardType rewardType, String description) {
        this.id = id;
        this.amount = amount;
        this.rewardType = rewardType;
        this.description = description;
    }
}
