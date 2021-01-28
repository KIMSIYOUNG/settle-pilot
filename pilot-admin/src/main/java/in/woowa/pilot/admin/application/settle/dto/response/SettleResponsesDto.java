package in.woowa.pilot.admin.application.settle.dto.response;

import in.woowa.pilot.admin.common.CustomPageImpl;
import in.woowa.pilot.core.settle.Settle;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class SettleResponsesDto {
    private CustomPageImpl<SettleResponseDto> settles;

    public SettleResponsesDto(Page<Settle> settles) {
        List<SettleResponseDto> content = settles.getContent().stream()
                .map(SettleResponseDto::new)
                .collect(Collectors.toList());

        this.settles = new CustomPageImpl<>(content, settles.getPageable(), settles.getTotalElements());
    }
}
