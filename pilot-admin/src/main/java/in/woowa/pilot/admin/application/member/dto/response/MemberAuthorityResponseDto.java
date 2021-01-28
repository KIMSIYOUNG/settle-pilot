package in.woowa.pilot.admin.application.member.dto.response;

import in.woowa.pilot.core.member.AuthProvider;
import in.woowa.pilot.core.member.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberAuthorityResponseDto {
    private Long authorityId;
    private Role target;
    private Long memberId;
    private String name;
    private String email;
    private AuthProvider provider;
    private Role role;

    @Builder
    public MemberAuthorityResponseDto(
            Long authorityId,
            Role target,
            Long memberId,
            String name,
            String email,
            AuthProvider provider,
            Role role
    ) {
        this.authorityId = authorityId;
        this.target = target;
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.role = role;
    }
}
