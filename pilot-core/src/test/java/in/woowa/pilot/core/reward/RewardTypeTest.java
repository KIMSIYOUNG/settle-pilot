package in.woowa.pilot.core.reward;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class RewardTypeTest {

    @DisplayName("마이너스 인스턴스를 반환한다.")
    @Test
    void isMinus() {
        // when, then
        assertAll(
                () -> Assertions.assertThat(RewardType.ABUSING.isMinus()).isTrue(),
                () -> assertThat(RewardType.ETC.isMinus()).isFalse(),
                () -> assertThat(RewardType.DELIVERY_ACCIDENT.isMinus()).isFalse(),
                () -> assertThat(RewardType.SYSTEM_ERROR.isMinus()).isFalse()
        );
    }
}