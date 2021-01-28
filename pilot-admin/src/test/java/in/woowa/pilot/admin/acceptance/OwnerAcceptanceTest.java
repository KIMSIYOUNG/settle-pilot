package in.woowa.pilot.admin.acceptance;

import in.woowa.pilot.admin.application.owner.dto.response.OwnerPagedResponsesDto;
import in.woowa.pilot.admin.application.owner.dto.response.OwnerResponseDto;
import in.woowa.pilot.admin.presentation.owner.dto.request.OwnerCreateRequestDto;
import in.woowa.pilot.admin.util.Owners;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OwnerAcceptanceTest extends ApiCall {
    private static final String PATH = "/api/owners";

    /**
     * 업주 관리
     * <p>
     * 1. 관리자는 업주를 생성할 수 있다.
     * 2. 관리자 및 일반회원은 생성된 업주를 찾을 수 있다.
     * 3. 2명의 동일한 이름을 가진 업주, 1명의 다른 이름의 업주를 생성한다.
     * 4. 이름을 기준으로 조회한다.
     * 5. 조회한 업주중 한명에 대해 관리자는 정보를 수정한다.
     * 6. 관리자는 해당 업주를 삭제한다.
     */
    @DisplayName("업주 인수테스트(해피 케이스) - 업주에 대한 API를 관리한다.")
    @Test
    void manageOwner() throws Exception {
        Runnable 관리자회원 = 관리자회원세션();
        Runnable 일반회원 = 일반회원세션();

        OwnerCreateRequestDto 업주생성_요청데이터 = Owners.webCreateRequestDto();
        String 생성된업주_리소스 = 데이터생성요청(관리자회원, 업주생성_요청데이터, PATH);
        OwnerResponseDto 받아온_실제업주데이터 = 데이터요청(일반회원, OwnerResponseDto.class, PATH + getPathVariableBy(생성된업주_리소스));
        업주검증_요청데이터_실제데이터(업주생성_요청데이터, 받아온_실제업주데이터);

        세명의업주생성_두명은이름이_김_한명은_이(관리자회원, "KIM", "LEE");
        MultiValueMap<String, String> 업주검색조건 = new LinkedMultiValueMap<>();
        업주검색조건.add("name", "KIM");
        Page<OwnerResponseDto> 실제데이터_추가조회한업주들 = 데이터요청(일반회원, OwnerPagedResponsesDto.class, PATH, 업주검색조건).getOwners();
        assertThat(실제데이터_추가조회한업주들.getTotalElements()).isEqualTo(2);

        OwnerResponseDto 수정되기전업주 = 실제데이터_추가조회한업주들.getContent().get(0);
        String 수정된리소스 = 데이터전체_수정요청(관리자회원, Owners.webCreateUpdateRequest(수정되기전업주.getId()), PATH);
        String 수정된리소스의_요청경로 = PATH + getPathVariableBy(수정된리소스);
        OwnerResponseDto 수정된업주 = 데이터요청(일반회원, OwnerResponseDto.class, 수정된리소스의_요청경로);
        업주검증_수정검증(수정되기전업주, 수정된업주);

        데이터삭제요청(관리자회원, 수정된리소스의_요청경로);
        데이서요청및_응답코드확인(일반회원, status().isNotFound(), 수정된리소스의_요청경로);
    }

    private void 세명의업주생성_두명은이름이_김_한명은_이(Runnable session, String twoNames, String oneName) throws Exception {
        데이터생성요청(session, Owners.webCreateRequestDto(twoNames, "KIM1@KIM.COM"), PATH);
        데이터생성요청(session, Owners.webCreateRequestDto(twoNames, "KIM2@KIM.COM"), PATH);
        데이터생성요청(session, Owners.webCreateRequestDto(oneName, "LEE@KIM.COM"), PATH);
    }

    private void 업주검증_요청데이터_실제데이터(OwnerCreateRequestDto requestDto, OwnerResponseDto responseDto) {
        assertAll(
                () -> assertThat(responseDto.getId()).isNotNull(),
                () -> assertThat(responseDto.getAccount().getAccountType()).isEqualTo(requestDto.getAccount().getAccountType()),
                () -> assertThat(responseDto.getAccount().getAccountNumber()).isEqualTo(requestDto.getAccount().getAccountNumber().getAccountNumber()),
                () -> assertThat(responseDto.getSettleType()).isEqualTo(requestDto.getSettleType()),
                () -> assertThat(responseDto.getName()).isEqualTo(requestDto.getName()),
                () -> assertThat(responseDto.getEmail()).isEqualTo(requestDto.getEmail())
        );
    }

    private void 업주검증_수정검증(OwnerResponseDto before, OwnerResponseDto after) {
        assertAll(
                () -> assertThat(before).isEqualToIgnoringGivenFields(after, "name", "email", "account"),
                () -> assertThat(after.getName()).isEqualTo(Owners.CHANGED_NAME),
                () -> assertThat(after.getEmail()).isEqualTo(Owners.CHANGED_EMAIL)
        );
    }
}
