package in.woowa.pilot.admin.application.settle.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SettleSnapshotSearchDto {
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;

    @Builder
    public SettleSnapshotSearchDto(LocalDateTime startAt, LocalDateTime endAt) {
        if ((startAt != null && endAt != null) && startAt.isAfter(endAt)) {
            throw new IllegalArgumentException("시작일은 종료일보다 이전일 수 없습니다.");
        }

        this.startAt = startAt;
        this.endAt = endAt;
    }
}
