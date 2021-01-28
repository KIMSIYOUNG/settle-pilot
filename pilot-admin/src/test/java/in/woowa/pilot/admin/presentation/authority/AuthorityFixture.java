package in.woowa.pilot.admin.presentation.authority;

import in.woowa.pilot.admin.application.authority.dto.AuthorityResponseDto;
import in.woowa.pilot.core.authority.Authority;
import in.woowa.pilot.core.authority.AuthorityStatus;
import in.woowa.pilot.core.member.Member;
import in.woowa.pilot.core.member.Role;
import in.woowa.pilot.fixture.member.MemberFixture;

public class AuthorityFixture {

    public static Authority createWithoutId() {
        return createWithoutId(MemberFixture.createWithId());
    }

    public static Authority createWithoutId(Member member) {
        return Authority.testBuilder()
                .member(member)
                .authorityStatus(AuthorityStatus.PENDING)
                .target(Role.ADMIN)
                .build();
    }

    public static AuthorityResponseDto createResponse() {
        return new AuthorityResponseDto(createWithoutId());
    }
}
