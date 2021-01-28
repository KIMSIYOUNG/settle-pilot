package in.woowa.pilot.admin.presentation.member.dto.request;

import in.woowa.pilot.admin.application.member.dto.request.MemberSearchDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberSearchRequestDto {
    private Long id;
    private String name;
    private String email;

    public MemberSearchDto toServiceCondition() {
        return MemberSearchDto.builder()
                .id(id)
                .name(name)
                .email(email)
                .build();
    }
}
