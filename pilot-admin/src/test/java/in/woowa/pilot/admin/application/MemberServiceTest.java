package in.woowa.pilot.admin.application;

import in.woowa.pilot.admin.application.member.MemberService;
import in.woowa.pilot.admin.application.member.dto.request.MemberChangeRoleDto;
import in.woowa.pilot.admin.application.member.dto.request.MemberCreateDto;
import in.woowa.pilot.admin.application.member.dto.response.MemberResponseDto;
import in.woowa.pilot.admin.common.exception.ResourceNotFoundException;
import in.woowa.pilot.admin.util.Members;
import in.woowa.pilot.core.authority.AuthorityRepository;
import in.woowa.pilot.core.member.Member;
import in.woowa.pilot.core.member.MemberRepository;
import in.woowa.pilot.core.member.Role;
import in.woowa.pilot.fixture.member.MemberFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class MemberServiceTest extends IntegrationTest {
    private MemberCreateDto request;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @BeforeEach
    void setUp() {
        request = Members.createRequestDto();
    }

    @DisplayName("회원을 정상적으로 생성할 수 있다. 정상적으로 생성된 회원의 Role은 NORMAL이다.")
    @Test
    void create() {
        // when
        MemberResponseDto response = memberService.create(request);
        // then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getRole()).isEqualTo(Role.NORMAL)
        );
    }

    @DisplayName("회원 생성이후 해당 회원을 조회하면 정상적으로 조회된다.")
    @Test
    void findById() {
        // given
        MemberResponseDto expected = memberService.create(request);
        // when
        MemberResponseDto actual = memberService.findById(expected.getId());
        // then
        assertThat(expected).usingRecursiveComparison().isEqualTo(actual);
    }

    @DisplayName("사용자가 존재하는 경우, 존재하는 사용자를 찾아온다.")
    @Test
    void find() {
        // given
        MemberResponseDto expected = memberService.create(request);
        // when
        MemberResponseDto actual = memberService.findOrElseCreate(
                expected.getEmail(),
                expected.getProvider(),
                request
        );
        // then
        assertThat(expected).usingRecursiveComparison().isEqualTo(actual);
    }

    @DisplayName("저장된 사용자가 없으면 새로운 사용자를 생성한다.")
    @Test
    void findOrElseCreate() {
        // when
        MemberResponseDto response = memberService.findOrElseCreate(
                request.getEmail(),
                request.getAuthProvider(),
                request
        );
        // then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getRole()).isEqualTo(Role.NORMAL)
        );
    }

    @DisplayName("관리자는 임의로 회원의 권한을 변경할 수 있다.")
    @Test
    void changeMemberRole() {
        Member expected = memberRepository.save(MemberFixture.createNormalWithoutId());
        // when
        MemberChangeRoleDto request = Members.appChangeRoleDto(expected.getId());
        memberService.changeMemberRole(request);
        MemberResponseDto actual = memberService.findById(expected.getId());
        // then
        assertThat(actual.getRole()).isEqualTo(request.getRole());
    }

    @DisplayName("삭제한 회원은 ID를 통해 조회되지 않는다.")
    @Test
    void findActiveMemberById() {
        // given
        MemberResponseDto savedMember = memberService.create(Members.createRequestDto());
        // when
        memberService.deleteById(savedMember.getId());
        // then
        assertThatThrownBy(() -> memberService.findById(savedMember.getId()))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}