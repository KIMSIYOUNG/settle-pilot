package in.woowa.pilot.admin.presentation.reward.dto.request;

import in.woowa.pilot.admin.application.reward.dto.request.RewardPeriodDto;
import in.woowa.pilot.core.reward.RewardType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RewardPeriodCreateRequestDto {

    @NotNull(message = "보상타입은 필수항목입니다.")
    private RewardType rewardType;

    @NotBlank(message = "보상 상세설명은 필수항목입니다.")
    private String description;

    @NotNull(message = "보상 예정일은 필수항목입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime rewardDateTime;

    @NotNull(message = "보상시작시점은 필수 항목입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startAt;

    @NotNull(message = "보상종료시점은 필수 항목입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endAt;

    @Builder(builderClassName = "testBuilder", builderMethodName = "testBuilder")
    public RewardPeriodCreateRequestDto(
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

    public RewardPeriodDto toServiceDto() {
        return RewardPeriodDto.builder()
                .rewardType(rewardType)
                .description(description)
                .rewardDateTime(rewardDateTime)
                .startAt(startAt)
                .endAt(endAt)
                .build();
    }
}
