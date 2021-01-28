package in.woowa.pilot.core.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class OrderDetailTest {

    @DisplayName("결제타입 및 금액을 총합산하여 금액을 도출할 수 있다." +
            "1,000 + 10,000 - 2,000 + 2,000 = 11,000")
    @Test
    void calculateAmount() {
        // given
        OrderDetails orderDetails = new OrderDetails(Arrays.asList(
                OrderDetail.builder()
                        .paymentType(PaymentType.POINT)
                        .amount(BigDecimal.valueOf(1000))
                        .build(),
                OrderDetail.builder()
                        .paymentType(PaymentType.CARD)
                        .amount(BigDecimal.valueOf(10000))
                        .build(),
                OrderDetail.builder()
                        .paymentType(PaymentType.COUPON)
                        .paymentOption(PaymentOption.OWNER_COUPON)
                        .amount(BigDecimal.valueOf(2000))
                        .build(),
                OrderDetail.builder()
                        .paymentType(PaymentType.COUPON)
                        .paymentOption(PaymentOption.BAEMIN_COUPON)
                        .amount(BigDecimal.valueOf(2000))
                        .build()
        ), null);
        // when
        BigDecimal amount = orderDetails.createOrderAmount();
        // then
        assertThat(amount).isEqualTo(BigDecimal.valueOf(11000));
    }
}