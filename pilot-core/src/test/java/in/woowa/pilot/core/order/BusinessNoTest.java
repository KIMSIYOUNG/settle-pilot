package in.woowa.pilot.core.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BusinessNoTest {

    @DisplayName("주문시각이 없으면 예외를 반환한다.")
    @Test
    void withoutOrderDate() {
        assertThatThrownBy(() -> new BusinessNo(null, "RANDOM_STRING"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("BusinessNo : 주문일시는 필수사항입니다.");
    }

    @DisplayName("난수가 없으면 예외를 반환한다.")
    @Test
    void withoutRandom() {
        assertThatThrownBy(() -> new BusinessNo(LocalDate.now(), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("BusinessNo : 난수는 필수사항입니다.");
    }

    @DisplayName("정상적으로 주문번호를 생성한다.")
    @Test
    void create() {
        BusinessNo businessNo = new BusinessNo(LocalDate.now(), "RANDOM_STRING");
        String format = LocalDate.now().format(DateTimeFormatter.ofPattern("YYYYMMDD"));

        assertThat(businessNo.getBusinessNo()).isEqualTo(format + "RANDOM_STRING");
    }
}