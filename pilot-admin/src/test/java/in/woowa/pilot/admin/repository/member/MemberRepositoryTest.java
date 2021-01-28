package in.woowa.pilot.admin.repository.member;

import in.woowa.pilot.admin.application.IntegrationTest;
import in.woowa.pilot.admin.application.member.dto.request.MemberSearchDto;
import in.woowa.pilot.admin.application.member.dto.response.MemberAuthorityResponseDto;
import in.woowa.pilot.admin.presentation.authority.AuthorityFixture;
import in.woowa.pilot.core.authority.Authority;
import in.woowa.pilot.core.authority.AuthorityRepository;
import in.woowa.pilot.core.member.AuthProvider;
import in.woowa.pilot.core.member.Member;
import in.woowa.pilot.core.member.MemberRepository;
import in.woowa.pilot.core.member.Role;
import in.woowa.pilot.fixture.BaseFixture;
import in.woowa.pilot.fixture.member.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MemberRepositoryTest extends IntegrationTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    MemberCustomRepository memberCustomRepository;

    @DisplayName("회원과 권한을 모두 가져올 수 있다.")
    @Test
    void fetchMemberPagedResponse() {
        Member member1 = memberRepository.save(MemberFixture.createNormalWithoutId());
        Member member2 = memberRepository.save(MemberFixture.createNormalWithoutId());
        memberRepository.save(MemberFixture.createNormalWithoutId());
        memberRepository.save(MemberFixture.createNormalWithoutId());
        authorityRepository.save(AuthorityFixture.createWithoutId(member1));
        authorityRepository.save(AuthorityFixture.createWithoutId(member2));
        // when
        Page<MemberAuthorityResponseDto> response = memberCustomRepository.fetchMemberWithAuthorities(
                MemberSearchDto.builder().build(),
                BaseFixture.DEFAULT_PAGEABLE
        );
        List<MemberAuthorityResponseDto> content = response.getContent();
        // then
        assertAll(
                () -> assertThat(response.getTotalElements()).isEqualTo(4),
                () -> assertThat(content).filteredOn((ma) -> ma.getAuthorityId() == null).hasSize(2),
                () -> assertThat(content).filteredOn((ma) -> ma.getAuthorityId() != null).hasSize(2),
                () -> assertThat(content).filteredOn((ma) -> ma.getTarget() == Role.ADMIN).hasSize(2)
        );
    }

    @DisplayName("이름과 일부 일치하는 회원을 조회한다.")
    @Test
    void fetchMemberNamePagedWithCondition() {
        // given
        Member member1 = memberRepository.save(Member.builder()
                .name("김시영")
                .email("siyoung@woowahan.com")
                .provider(AuthProvider.GOOGLE)
                .build());
        Member member2 = memberRepository.save(Member.builder()
                .name("장재주")
                .email("jaeju@woowahan.com")
                .provider(AuthProvider.GOOGLE)
                .build());
        Authority authority = authorityRepository.save(AuthorityFixture.createWithoutId(member1));
        // when
        MemberSearchDto condition = new MemberSearchDto(null, "시영", "");
        Page<MemberAuthorityResponseDto> response = memberCustomRepository.fetchMemberWithAuthorities(condition, BaseFixture.DEFAULT_PAGEABLE);
        List<MemberAuthorityResponseDto> content = response.getContent();
        // then
        assertAll(
                () -> assertThat(response.getTotalElements()).isEqualTo(1),
                () -> assertThat(content.get(0).getAuthorityId()).isEqualTo(authority.getId()),
                () -> assertThat(content.get(0).getMemberId()).isEqualTo(member1.getId())
        );
    }

    @DisplayName("이메일이 일부 일치하는 회원을 조회한다.")
    @Test
    void fetchMemberEmailPagedWithCondition() {
        // given
        Member member1 = memberRepository.save(Member.builder()
                .name("김시영")
                .email("siyoung@woowahan.com")
                .provider(AuthProvider.GOOGLE)
                .build());
        Member member2 = memberRepository.save(Member.builder()
                .name("장재주")
                .email("jaeju@woowahan.com")
                .provider(AuthProvider.GOOGLE)
                .build());
        authorityRepository.save(AuthorityFixture.createWithoutId(member1));
        // when
        MemberSearchDto condition = new MemberSearchDto(null, "", "jaeju");
        Page<MemberAuthorityResponseDto> response = memberCustomRepository.fetchMemberWithAuthorities(condition, BaseFixture.DEFAULT_PAGEABLE);
        List<MemberAuthorityResponseDto> content = response.getContent();
        // then
        assertAll(
                () -> assertThat(response.getTotalElements()).isEqualTo(1),
                () -> assertThat(content.get(0).getAuthorityId()).isNull(),
                () -> assertThat(content.get(0).getMemberId()).isEqualTo(member2.getId())
        );
    }

}
