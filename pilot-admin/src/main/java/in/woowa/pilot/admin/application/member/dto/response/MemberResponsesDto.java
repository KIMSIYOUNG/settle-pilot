package in.woowa.pilot.admin.application.member.dto.response;

import in.woowa.pilot.admin.common.CustomPageImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
public class MemberResponsesDto {
    private CustomPageImpl<MemberAuthorityResponseDto> members;

    public MemberResponsesDto(Page<MemberAuthorityResponseDto> response) {
        this.members = new CustomPageImpl<>(
                response.getContent(),
                response.getPageable(),
                response.getTotalElements()
        );
    }
}
