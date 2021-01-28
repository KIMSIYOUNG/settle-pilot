package in.woowa.pilot.admin.presentation.reward.dto.request;

import in.woowa.pilot.admin.application.reward.dto.request.RewardCreateDto;
import in.woowa.pilot.core.reward.RewardType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RewardCreateRequestDto {

    @NotNull(message = "금액은 필수항목입니다.")
    private BigDecimal amount;

    @NotNull(message = "보상타입은 필수항목입니다.")
    private RewardType rewardType;

    @NotBlank(message = "보상 상세설명은 필수항목입니다.")
    private String description;

    @NotNull(message = "보상 대상 업주는 필수항목입니다.")
    private Long ownerId;

    @NotNull(message = "보상 예정일은 필수항목입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime rewardDateTime;

    public RewardCreateRequestDto(
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

    public RewardCreateDto toServiceDto() {
        return RewardCreateDto.builder()
                .amount(amount)
                .rewardType(rewardType)
                .description(description)
                .ownerId(ownerId)
                .rewardDateTime(rewardDateTime)
                .build();
    }
}
