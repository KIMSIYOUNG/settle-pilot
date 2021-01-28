package in.woowa.pilot.admin.application.authority.dto;

import in.woowa.pilot.core.authority.Authority;
import in.woowa.pilot.core.authority.AuthorityStatus;
import in.woowa.pilot.core.member.Member;
import in.woowa.pilot.core.member.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthorityDto {
    private final Long memberId;
    private final Role role;

    @Builder
    public AuthorityDto(Long memberId, Role role) {
        this.memberId = memberId;
        this.role = role;
    }

    public Authority toAuthority(Member member) {
        return Authority.builder()
                .member(member)
                .target(role)
                .authorityStatus(AuthorityStatus.PENDING)
                .build();
    }
}
