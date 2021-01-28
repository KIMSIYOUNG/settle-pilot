package in.woowa.pilot.admin.presentation.authority;

import in.woowa.pilot.admin.application.authority.dto.AuthorityDto;
import in.woowa.pilot.core.member.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class AuthorityRequestDto {

    @NotNull(message = "변경하고자 하는 회원의 ID는 필수입니다.")
    private Long memberId;

    @NotNull(message = "변경하고 싶은 권한은 필수입니다.")
    private Role role;

    public AuthorityRequestDto(Long memberId, Role role) {
        this.memberId = memberId;
        this.role = role;
    }

    public AuthorityDto toServiceDto() {
        return AuthorityDto.builder()
                .memberId(memberId)
                .role(role)
                .build();
    }
}
