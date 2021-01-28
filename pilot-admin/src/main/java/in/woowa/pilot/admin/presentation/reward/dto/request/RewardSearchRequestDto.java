package in.woowa.pilot.admin.presentation.reward.dto.request;

import in.woowa.pilot.admin.application.reward.dto.request.RewardSearchDto;
import in.woowa.pilot.core.reward.RewardType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class RewardSearchRequestDto {
    private Long ownerId;

    private String rewardNo;

    private String ownerName;

    private String ownerEmail;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startAt;

    private RewardType rewardType;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endAt;

    public RewardSearchDto toAppCondition() {
        return RewardSearchDto.builder()
                .ownerId(ownerId)
                .rewardNo(rewardNo)
                .ownerName(ownerName)
                .ownerEmail(ownerEmail)
                .startAt(startAt)
                .endAt(endAt)
                .rewardType(rewardType)
                .build();
    }
}
