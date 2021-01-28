package in.woowa.pilot.admin.presentation.reward.dto.request;

import in.woowa.pilot.admin.application.reward.dto.request.RewardUpdateDto;
import in.woowa.pilot.core.reward.RewardType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class RewardUpdateRequestDto {

    @NotNull(message = "수정할 보상금액의 ID는 필수항목입니다.")
    private Long id;

    @NotNull(message = "보상금액은 필수항목입니다.")
    private BigDecimal amount;

    @NotNull(message = "보상타입은 필수항목입니다.")
    private RewardType rewardType;

    @NotBlank(message = "보상 상세설명은 필수항목입니다.")
    private String description;

    @Builder(builderClassName = "testBuilder", builderMethodName = "testBuilder")
    public RewardUpdateRequestDto(
            Long id,
            BigDecimal amount,
            RewardType rewardType,
            String description
    ) {
        this.id = id;
        this.amount = amount;
        this.rewardType = rewardType;
        this.description = description;
    }

    public RewardUpdateDto toServiceDto() {
        return RewardUpdateDto.builder()
                .id(id)
                .amount(amount)
                .rewardType(rewardType)
                .description(description)
                .build();
    }
}
