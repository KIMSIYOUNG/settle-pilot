package in.woowa.pilot.core.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderStatusTest {

    @DisplayName("OrderStatus 각각의 이름과, 할인율을 반환한다.")
    @Test
    void rateAndName() {
        assertAll(
                () -> Assertions.assertThat(OrderStatus.ORDER.getName()).isEqualTo("주문"),
                () -> assertThat(OrderStatus.ORDER_CONFIRM.getName()).isEqualTo("주문확인"),
                () -> assertThat(OrderStatus.DELIVERY.getName()).isEqualTo("배달중"),
                () -> assertThat(OrderStatus.DELIVERY_CONFIRM.getName()).isEqualTo("배달완료"),
                () -> assertThat(OrderStatus.CANCEL.getName()).isEqualTo("주문취소")
        );
    }
}