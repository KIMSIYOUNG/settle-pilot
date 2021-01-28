package in.woowa.pilot.admin.acceptance;

import in.woowa.pilot.admin.application.member.dto.response.MemberResponseDto;
import in.woowa.pilot.admin.presentation.authority.AuthorityRequestDto;
import in.woowa.pilot.core.member.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberAcceptanceTest extends ApiCall {
    private static final String PATH = "/api/members";
    private static final String REQUEST_ROLE_PATH = "/api/authorities";
    private static final String ROLE_APPROVAL_PATH = "/api/authorities/approve/";

    /**
     * 회원 관리
     * <p>
     * 1. 회원을 저장하고 토큰을 받을 수 있다.
     * 2. 토큰을 기반으로 사용자를 찾아올 수 있다.
     * 3. 찾아온 사용자의 이름을 변경할 수 있다.
     * 4. 사용자는 ADMIN 권한을 요청할 수 있다.
     * 5. 관리자가 로그인하여 이를 승인할 수 있다.
     * 6. 사용자는 계정을 삭제할 수 있다.
     */
    @DisplayName("회원 인수테스트(해피 케이스) - 회원에 대한 API를 관리한다.")
    @Test
    void 인수테스트_회원() throws Exception {
        Runnable 일반회원 = 일반회원세션();
        Runnable 관리자회원 = 관리자회원세션();

        MemberResponseDto 생성된회원 = 데이터요청(관리자회원, MemberResponseDto.class, PATH);
        AuthorityRequestDto 수정요청할_역할 = new AuthorityRequestDto(생성된회원.getId(), Role.ADMIN);
        String authorityPath = 데이터생성요청(일반회원, 수정요청할_역할, REQUEST_ROLE_PATH);
        데이터_수정_요청(관리자회원, ROLE_APPROVAL_PATH + getIdBy(authorityPath));
        MemberResponseDto 역할이수정된회원 = 데이터요청(일반회원, MemberResponseDto.class, PATH);
        역할검증_기존회원_수정된회원(생성된회원, 역할이수정된회원);

        데이터삭제요청(일반회원, PATH);
        데이서요청및_응답코드확인(일반회원, status().isNotFound(), PATH);
    }

    private void 역할검증_기존회원_수정된회원(MemberResponseDto nameChangedMember, MemberResponseDto roleChangedMember) {
        assertAll(
                () -> assertThat(roleChangedMember.getRole()).isEqualTo(Role.ADMIN),
                () -> assertThat(nameChangedMember).isEqualToIgnoringGivenFields(roleChangedMember, "role")
        );
    }
}
