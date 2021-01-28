package in.woowa.pilot.admin.presentation.reward.dto.request;

import in.woowa.pilot.admin.application.reward.dto.request.RewardOrdersCreateDto;
import in.woowa.pilot.core.reward.RewardType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class RewardOrdersCreateRequestDto {

    @NotNull(message = "주문번호는 필수항목입니다.")
    private List<Long> orderIds;

    @NotNull(message = "보상금액 타입은 필수항목입니다.")
    private RewardType rewardType;

    @NotBlank(message = "상세설명은 필수항목입니다.")
    private String description;

    @NotNull(message = "보상금액 예정기간은 필수항목입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime rewardDateTime;

    @Builder(builderClassName = "testBuilder", builderMethodName = "testBuilder")
    public RewardOrdersCreateRequestDto(
            List<Long> orderIds,
            RewardType rewardType,
            String description,
            LocalDateTime rewardDateTime
    ) {
        this.orderIds = orderIds;
        this.rewardType = rewardType;
        this.description = description;
        this.rewardDateTime = rewardDateTime;
    }

    public RewardOrdersCreateDto toServiceDto() {
        return RewardOrdersCreateDto.builder()
                .orderIds(orderIds)
                .rewardDateTime(rewardDateTime)
                .rewardType(rewardType)
                .description(description)
                .build();
    }
}
