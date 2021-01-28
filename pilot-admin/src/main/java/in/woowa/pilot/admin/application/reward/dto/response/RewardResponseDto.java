package in.woowa.pilot.admin.application.reward.dto.response;

import in.woowa.pilot.admin.application.order.dto.response.OrderResponseDto;
import in.woowa.pilot.admin.application.owner.dto.response.OwnerResponseDto;
import in.woowa.pilot.core.reward.Reward;
import in.woowa.pilot.core.reward.RewardType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RewardResponseDto {
    private Long id;
    private String rewardNo;
    private BigDecimal amount;
    private RewardType rewardType;
    private String description;
    private OwnerResponseDto owner;
    private OrderResponseDto order;
    private LocalDateTime rewardDateTime;

    public RewardResponseDto(Reward reward) {
        this.id = reward.getId();
        this.rewardNo = reward.getBusinessNo().getBusinessNo();
        this.amount = reward.getAmount();
        this.rewardType = reward.getRewardType();
        this.description = reward.getDescription();
        this.owner = new OwnerResponseDto(reward.getOwner());
        if (reward.getOrder() != null) {
            this.order = new OrderResponseDto(reward.getOrder());
        }
        this.rewardDateTime = reward.getRewardDateTime();
    }
}
