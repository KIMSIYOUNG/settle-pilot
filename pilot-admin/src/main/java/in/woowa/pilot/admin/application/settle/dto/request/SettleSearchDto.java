package in.woowa.pilot.admin.application.settle.dto.request;

import in.woowa.pilot.core.settle.SettleStatus;
import in.woowa.pilot.core.settle.SettleType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SettleSearchDto {
    private final Long ownerId;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final SettleStatus settleStatus;
    private final SettleType settleType;

    @Builder
    public SettleSearchDto(
            Long ownerId,
            LocalDateTime startAt,
            LocalDateTime endAt,
            SettleStatus settleStatus,
            SettleType settleType
    ) {
        if ((startAt != null && endAt != null) && startAt.isAfter(endAt)) {
            throw new IllegalArgumentException("시작일은 종료일보다 이전일 수 없습니다.");
        }

        this.ownerId = ownerId;
        this.startAt = startAt;
        this.endAt = endAt;
        this.settleStatus = settleStatus;
        this.settleType = settleType;
    }
}
