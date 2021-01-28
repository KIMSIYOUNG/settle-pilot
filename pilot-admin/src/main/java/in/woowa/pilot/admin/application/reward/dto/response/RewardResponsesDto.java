package in.woowa.pilot.admin.application.reward.dto.response;

import in.woowa.pilot.admin.common.CustomPageImpl;
import in.woowa.pilot.core.reward.Reward;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class RewardResponsesDto {
    private CustomPageImpl<RewardResponseDto> rewards;

    public RewardResponsesDto(Page<Reward> rewards) {
        List<RewardResponseDto> content = rewards.getContent().stream()
                .map(RewardResponseDto::new)
                .collect(Collectors.toList());

        this.rewards = new CustomPageImpl<>(
                content,
                rewards.getPageable(),
                rewards.getTotalElements()
        );
    }
}
