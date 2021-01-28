package in.woowa.pilot.admin.application.reward.dto.request;

import in.woowa.pilot.core.reward.RewardType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RewardSearchDto {
    private final Long ownerId;
    private final String rewardNo;
    private final String ownerName;
    private final String ownerEmail;
    private final LocalDateTime startAt;
    private final RewardType rewardType;
    private final LocalDateTime endAt;

    @Builder
    public RewardSearchDto(
            Long ownerId,
            String rewardNo,
            String ownerName,
            String ownerEmail,
            LocalDateTime startAt,
            LocalDateTime endAt,
            RewardType rewardType
    ) {
        if ((startAt != null && endAt != null) && startAt.isAfter(endAt)) {
            throw new IllegalArgumentException("시작일은 종료일보다 이전일 수 없습니다.");
        }

        this.ownerId = ownerId;
        this.rewardNo = rewardNo;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.startAt = startAt;
        this.rewardType = rewardType;
        this.endAt = endAt;
    }
}
