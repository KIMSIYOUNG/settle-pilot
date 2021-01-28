package in.woowa.pilot.admin.presentation.settle.dto.request;


import in.woowa.pilot.admin.application.settle.dto.request.SettleSearchDto;
import in.woowa.pilot.core.settle.SettleStatus;
import in.woowa.pilot.core.settle.SettleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class SettleSearchRequestDto {

    private Long ownerId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endAt;

    private SettleStatus settleStatus;

    private SettleType settleType;

    public SettleSearchRequestDto(Long ownerId, LocalDateTime startAt, LocalDateTime endAt, SettleStatus settleStatus) {
        this.ownerId = ownerId;
        this.startAt = startAt;
        this.endAt = endAt;
        this.settleStatus = settleStatus;
    }

    public SettleSearchDto toAppCondition() {
        return SettleSearchDto.builder()
                .ownerId(ownerId)
                .startAt(startAt)
                .endAt(endAt)
                .settleStatus(settleStatus)
                .settleType(settleType)
                .build();
    }
}
