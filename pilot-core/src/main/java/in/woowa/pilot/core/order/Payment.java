package in.woowa.pilot.core.order;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@Getter
@EqualsAndHashCode(of = {"paymentType", "paymentOption"})
public class Payment {
    private final PaymentType paymentType;
    private final PaymentOption paymentOption;

    public Payment(OrderDetail orderDetail) {
        this.paymentType = orderDetail.getPaymentType();
        this.paymentOption = orderDetail.getPaymentOption();
    }

    public Payment(OrderPaymentAggregation orderPaymentAggregation) {
        this.paymentType = orderPaymentAggregation.getPaymentType();
        this.paymentOption = orderPaymentAggregation.getPaymentOption();
    }

    public Payment(PaymentType paymentType, PaymentOption paymentOption) {
        Objects.requireNonNull(paymentType, "결제타입은 필수항목입니다.");
        Objects.requireNonNull(paymentOption, "결제옵션은 필수항목입니다.");

        this.paymentType = paymentType;
        this.paymentOption = paymentOption;
    }
}
