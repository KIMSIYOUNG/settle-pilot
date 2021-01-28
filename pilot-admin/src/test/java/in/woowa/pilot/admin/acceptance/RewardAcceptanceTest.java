package in.woowa.pilot.admin.acceptance;

import in.woowa.pilot.admin.application.reward.dto.response.RewardResponseDto;
import in.woowa.pilot.admin.presentation.reward.dto.request.RewardCreateRequestDto;
import in.woowa.pilot.admin.util.Owners;
import in.woowa.pilot.admin.util.Rewards;
import in.woowa.pilot.fixture.reward.RewardFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static in.woowa.pilot.fixture.TestUtils.assertThatBigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RewardAcceptanceTest extends ApiCall {

    /**
     * 보상금액 관리
     * <p>
     * 1. 관리자를 생성한다.
     * 2. 업주를 생성한다.
     * 3. 보상금액을 생성한다.
     * 4. 보상금액을 조회한다.
     * 5. 보상금액의 정보를 수정한다.
     * 6. 보상금액을 삭제한다.
     */
    @DisplayName("인수테스트(해피 케이스) - 보상금액을 관리한다.")
    @Test
    void manageReward() throws Exception {
        Runnable 관리자회원 = 관리자회원세션();

        String 생성된_업주리소스 = 데이터생성요청(관리자회원, Owners.webCreateRequestDto(), "/api/owners");

        RewardCreateRequestDto 보상금액_요청데이터 = Rewards.webCreateRequestDto(getIdBy(생성된_업주리소스));
        String 생성된_보상금액리소스 = 데이터생성요청(관리자회원, 보상금액_요청데이터, RewardFixture.RESOURCE);

        RewardResponseDto 받아온_보상금액데이터 = 데이터요청(관리자회원, RewardResponseDto.class, RewardFixture.RESOURCE + getPathVariableBy(생성된_보상금액리소스));
        보상금액검증_요청데이터_실제데이터(보상금액_요청데이터, 받아온_보상금액데이터);

        String 수정된_보상금액_리소스 = 데이터전체_수정요청(관리자회원, Rewards.webUpdateRequestDto(받아온_보상금액데이터.getId()), RewardFixture.RESOURCE);
        RewardResponseDto 수정된_보상금액데이터 = 데이터요청(관리자회원, RewardResponseDto.class, RewardFixture.RESOURCE + getPathVariableBy(수정된_보상금액_리소스));
        보상금액검증_수정전_수정후(받아온_보상금액데이터, 수정된_보상금액데이터);

        데이터삭제요청(관리자회원, RewardFixture.RESOURCE + getPathVariableBy(수정된_보상금액_리소스));
        데이서요청및_응답코드확인(관리자회원, status().isNotFound(), RewardFixture.RESOURCE + getPathVariableBy(수정된_보상금액_리소스));
    }

    private void 보상금액검증_요청데이터_실제데이터(RewardCreateRequestDto requestDto, RewardResponseDto responseDto) {
        assertAll(
                () -> assertThat(responseDto).isEqualToIgnoringGivenFields(requestDto, "id", "amount", "order", "owner", "rewardNo"),
                () -> assertThat(responseDto.getRewardNo()).isNotNull(),
                () -> assertThat(responseDto.getOwner().getId()).isEqualTo(requestDto.getOwnerId()),
                () -> assertThatBigDecimal(responseDto.getAmount()).isEqualTo(requestDto.getAmount())
        );
    }

    private void 보상금액검증_수정전_수정후(RewardResponseDto before, RewardResponseDto after) {
        assertAll(
                () -> assertThat(before.getId()).isEqualTo(after.getId()),
                () -> assertThat(after.getRewardType()).isEqualTo(Rewards.UPDATED_TYPE),
                () -> assertThat(after.getDescription()).isEqualTo(Rewards.UPDATED_MESSAGE),
                () -> assertThatBigDecimal(after.getAmount()).isEqualTo(Rewards.UPDATED_AMOUNT)
        );
    }
}
