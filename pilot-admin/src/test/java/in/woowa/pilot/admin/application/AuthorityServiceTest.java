package in.woowa.pilot.admin.application;

import in.woowa.pilot.admin.application.authority.AuthorityService;
import in.woowa.pilot.admin.application.authority.dto.AuthorityDto;
import in.woowa.pilot.admin.application.authority.dto.AuthorityResponseDto;
import in.woowa.pilot.core.authority.Authority;
import in.woowa.pilot.core.authority.AuthorityRepository;
import in.woowa.pilot.core.authority.AuthorityStatus;
import in.woowa.pilot.core.member.Member;
import in.woowa.pilot.core.member.MemberRepository;
import in.woowa.pilot.core.member.Role;
import in.woowa.pilot.fixture.member.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class AuthorityServiceTest extends IntegrationTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AuthorityService authorityService;

    @Autowired
    AuthorityRepository authorityRepository;

    @DisplayName("회원은 권한을 요청할 수 있다.")
    @Test
    void create() {
        // given
        Member member = memberRepository.save(MemberFixture.createNormalWithoutId());
        // when , then
        AuthorityResponseDto response = authorityService.create(new AuthorityDto(member.getId(), Role.ADMIN));
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getTarget()).isEqualTo(Role.ADMIN)
        );
    }

    @DisplayName("관리자는 권한 요청을 승인할 수 있다.")
    @Test
    void approve() {
        // given
        Member member = memberRepository.save(MemberFixture.createNormalWithoutId());
        // when
        AuthorityResponseDto response = authorityService.create(new AuthorityDto(member.getId(), Role.ADMIN));
        authorityService.approve(response.getId());
        // then
        Member findMember = memberRepository.findById(member.getId()).get();
        Authority findAuthority = authorityRepository.findById(response.getId()).get();

        assertAll(
                () -> assertThat(findMember.getRole()).isEqualTo(Role.ADMIN),
                () -> assertThat(findAuthority.getAuthorityStatus()).isEqualTo(AuthorityStatus.APPROVE)
        );
    }

    @DisplayName("관리자는 권한 요청을 거절할 수 있다.")
    @Test
    void reject() {
        // given
        Member member = memberRepository.save(MemberFixture.createNormalWithoutId());
        // when
        AuthorityResponseDto response = authorityService.create(new AuthorityDto(member.getId(), Role.ADMIN));
        authorityService.reject(response.getId());
        // then
        Member findMember = memberRepository.findById(member.getId()).get();
        Authority findAuthority = authorityRepository.findById(response.getId()).get();

        assertAll(
                () -> assertThat(findMember.getRole()).isEqualTo(Role.NORMAL),
                () -> assertThat(findAuthority.getAuthorityStatus()).isEqualTo(AuthorityStatus.REJECT)
        );
    }
}