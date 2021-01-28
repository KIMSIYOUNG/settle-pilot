package in.woowa.pilot.admin.application.settle.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class SettleCompleteConditionDto {
    private final Long ownerId;
    private final List<Long> settleIds;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;

    @Builder
    public SettleCompleteConditionDto(Long ownerId, List<Long> settleIds, LocalDateTime startAt, LocalDateTime endAt) {
        this.ownerId = ownerId;
        this.settleIds = settleIds;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public boolean notContainsCondition() {
        return ownerId == null
                && CollectionUtils.isEmpty(settleIds)
                && startAt == null
                && endAt == null;
    }
}
