package in.woowa.pilot.admin.presentation.settle.dto.request;

import in.woowa.pilot.admin.application.settle.dto.request.SettleCompleteConditionDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class SettleCompleteRequestDto {

    private Long ownerId;

    private List<Long> settleIds;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endAt;

    @Builder(builderClassName = "testBuilder", builderMethodName = "testBuilder")
    public SettleCompleteRequestDto(Long ownerId, List<Long> settleIds, LocalDateTime startAt, LocalDateTime endAt) {
        this.ownerId = ownerId;
        this.settleIds = settleIds;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public SettleCompleteConditionDto toAppCondition() {
        return SettleCompleteConditionDto.builder()
                .ownerId(ownerId)
                .settleIds(settleIds)
                .startAt(startAt)
                .endAt(endAt)
                .build();
    }
}
