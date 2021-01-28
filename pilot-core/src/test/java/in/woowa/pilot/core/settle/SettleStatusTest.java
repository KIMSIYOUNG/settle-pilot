package in.woowa.pilot.core.settle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SettleStatusTest {

    @DisplayName("존재하지 않는 지급금 상태는 예외를 반환한다.")
    @Test
    void nonExistStatus() {
        assertThatThrownBy(() -> SettleStatus.of("NON_EXIST"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 지급급 상태입니다.");
    }

    @DisplayName("지급금 상태를 대소문자 구분 없이 찾을 수 있다.")
    @Test
    void existStatus() {
        SettleStatus completed = SettleStatus.of("completeD");
        SettleStatus created = SettleStatus.of("CreaTeD");

        assertThat(completed).isEqualTo(SettleStatus.COMPLETED);
        assertThat(created).isEqualTo(SettleStatus.CREATED);
    }
}