package in.woowa.pilot.admin.acceptance;

import in.woowa.pilot.admin.application.settle.dto.response.SettleResponseDto;
import in.woowa.pilot.admin.application.settle.dto.response.SettleResponsesDto;
import in.woowa.pilot.admin.presentation.settle.dto.request.SettleInCaseCreateRequestDto;
import in.woowa.pilot.admin.util.Settles;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.settle.SettleStatus;
import in.woowa.pilot.core.settle.SettleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static in.woowa.pilot.fixture.TestUtils.assertThatBigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class SettleAcceptanceTest extends ApiCall {

    /**
     * 지급금을 관리한다.
     * <p>
     * 1. 관리자가 생성되어 있다.
     * 2. 주문과 보상금액이 생성되어 있다.
     * 3. 관리자는 지급금을 생성한다.
     * 4. 지급금을 조회한다.
     * 5. 조회한 지급금을 완료처리 한다.
     * 6. 지급금을 삭제한다.
     */
    @DisplayName("인수테스트(해피 케이스) - 지급금을 관리한다.")
    @Test
    void manageSettle() throws Exception {
        Runnable 관리자회원 = 관리자회원세션();
        Owner 업주 = 업주생성();
        주문생성(업주, 3, LocalDateTime.now().minusDays(10));
        보상금액생성(업주, 3, LocalDateTime.now().minusDays(10));

        SettleInCaseCreateRequestDto 지급금_요청데이터 = Settles.webCreateRequestDto(업주.getId(), SettleType.DAILY, LocalDate.now().minusDays(10));
        String 생성된_지급금_리소스 = 데이터생성요청(관리자회원, 지급금_요청데이터, Settles.RESOURCE);
        Page<SettleResponseDto> 실제지급금_데이터 = 데이터요청(관리자회원, SettleResponsesDto.class, Settles.RESOURCE + "/all").getSettles();
        지급금검증_요청데이터_실제데이터(지급금_요청데이터, 실제지급금_데이터);

        String 수정된_지급금_리소스 = 데이터_수정_요청(관리자회원, Settles.webUpdateRequestDto(getIdBy(생성된_지급금_리소스)), Settles.RESOURCE);
        Page<SettleResponseDto> 수정된_지급금_데이터 = 데이터요청(관리자회원, SettleResponsesDto.class, Settles.RESOURCE + "/all").getSettles();
        assertThat(수정된_지급금_데이터.getContent().get(0).getSettleStatus()).isEqualTo(SettleStatus.COMPLETED.name());

        데이터삭제요청(관리자회원, 수정된_지급금_리소스);
        Page<SettleResponseDto> 삭제이후_지급금데이터 = 데이터요청(관리자회원, SettleResponsesDto.class, Settles.RESOURCE + "/all").getSettles();
        assertThat(삭제이후_지급금데이터.getTotalElements()).isEqualTo(0);
    }

    private void 지급금검증_요청데이터_실제데이터(SettleInCaseCreateRequestDto request, Page<SettleResponseDto> response) {
        List<SettleResponseDto> content = response.getContent();
        SettleResponseDto actual = content.get(0);

        assertAll(
                () -> assertThat(request.getOwnerId()).isEqualTo(actual.getOwnerId()),
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThatBigDecimal(actual.getAmount()).isEqualTo(BigDecimal.valueOf(150_000))
        );
    }
}
