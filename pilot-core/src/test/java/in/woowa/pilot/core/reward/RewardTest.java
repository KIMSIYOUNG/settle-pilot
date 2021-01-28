package in.woowa.pilot.core.reward;

import in.woowa.pilot.fixture.owner.OwnerFixture;
import in.woowa.pilot.fixture.reward.RewardFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RewardTest {

    @DisplayName("비즈니스 번호가 없는 경우 빈 문자열을 반환한다.")
    @Test
    void businessNoValue() {
        // given
        Reward reward = Reward.builder()
                .amount(BigDecimal.valueOf(-1000))
                .rewardType(RewardType.SYSTEM_ERROR)
                .rewardDateTime(LocalDateTime.now())
                .description("시스템 오류")
                .owner(OwnerFixture.createWithoutId())
                .build();
        // when & then
        assertThat(reward.getBusinessNoValue()).isEqualTo("");
    }

    @DisplayName("어뷰징인 경우 양수를 입력할 수 없다.")
    @ParameterizedTest
    @ValueSource(doubles = {0.0001, 0.0000000001, 1, 10000})
    void createWithAbusing(double money) {
        // when, then
        assertThatThrownBy(() -> Reward.builder()
                .amount(BigDecimal.valueOf(money))
                .rewardType(RewardType.ABUSING)
                .description("어뷰징 처벌")
                .owner(OwnerFixture.createWithoutId())
                .rewardDateTime(LocalDateTime.now())
                .build()
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("어뷰징 금액은 0 혹은 음수만 가능합니다.");
    }

    @DisplayName("보정금액 생성일은 필수항목이다.")
    @Test
    void rewardDateTimeNull() {
        assertThatThrownBy(() -> Reward.builder()
                .amount(BigDecimal.valueOf(3000))
                .rewardType(RewardType.ABUSING)
                .description("어뷰징 처벌")
                .owner(OwnerFixture.createWithoutId())
                .rewardDateTime(null)
                .build()
        )
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("보정금액 생성일이 현재보다 이후이면 예외를 반환한다.(현재까지만 가능하다)")
    @Test
    void rewardDateTimeInvalid() {
        LocalDateTime afterCurrentTime = LocalDateTime.now().plusMinutes(1);

        assertThatThrownBy(() -> Reward.builder()
                .amount(BigDecimal.valueOf(3000))
                .rewardType(RewardType.ABUSING)
                .description("어뷰징 처벌")
                .owner(OwnerFixture.createWithoutId())
                .rewardDateTime(afterCurrentTime)
                .build()
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("보정금액의 생성시기는 현재 이전일 수 없습니다.");
    }

    @DisplayName("수정할 때 금액/타입/이유 중 하나라도 값이 없으면 예외를 반환한다.")
    @ParameterizedTest
    @MethodSource(value = "provideArgs")
    void update(BigDecimal amount, String type, String description) {
        // given
        Reward reward = RewardFixture.createWithoutId();
        // when , then
        assertThatThrownBy(() -> reward.update(amount, type, description))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("금액/타입/이유를 모두 입력해주세요.");
    }

    private static Stream<Arguments> provideArgs() {
        return Stream.of(
                Arguments.of(null, "RewardType", null),
                Arguments.of(BigDecimal.valueOf(2000), null, null),
                Arguments.of(null, null, "오토바이 사고 보상"),
                Arguments.of(null, "rewardType", "오토바이 사고 보상"),
                Arguments.of(BigDecimal.valueOf(1000), null, "오토바이 사고 보상")
        );
    }

    @DisplayName("어뷰징으로 업데이트 할 때 + 금액은 예외를 반환한다.")
    @Test
    void updateAbusing() {
        // given
        Reward reward = Reward.builder()
                .amount(BigDecimal.valueOf(1000))
                .rewardType(RewardType.SYSTEM_ERROR)
                .rewardDateTime(LocalDateTime.now())
                .description("시스템 오류")
                .owner(OwnerFixture.createWithoutId())
                .build();
        // when , then

        assertThatThrownBy(
                () -> reward.update(BigDecimal.valueOf(100), RewardType.ABUSING.name(), "어뷰징"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("어뷰징 금액은 0 혹은 음수만 가능합니다.");

    }
}