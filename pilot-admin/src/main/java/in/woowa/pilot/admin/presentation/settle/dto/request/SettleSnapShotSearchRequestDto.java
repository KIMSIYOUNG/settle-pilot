package in.woowa.pilot.admin.presentation.settle.dto.request;

import in.woowa.pilot.admin.application.settle.dto.request.SettleSnapshotSearchDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class SettleSnapShotSearchRequestDto {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endAt;

    public SettleSnapshotSearchDto toAppCondition() {
        return SettleSnapshotSearchDto.builder()
                .startAt(startAt)
                .endAt(endAt)
                .build();
    }
}
