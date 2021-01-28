package in.woowa.pilot.admin.application.order.dto.request;

import in.woowa.pilot.core.order.OrderDetail;
import in.woowa.pilot.core.order.PaymentOption;
import in.woowa.pilot.core.order.PaymentType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class OrderDetailDto {
    private final PaymentType paymentType;
    private final PaymentOption paymentOption;
    private final BigDecimal amount;

    @Builder
    public OrderDetailDto(PaymentType paymentType, PaymentOption paymentOption, BigDecimal amount) {
        this.paymentType = paymentType;
        this.paymentOption = paymentOption;
        this.amount = amount;
    }

    public OrderDetail toOrderDetail() {
        return OrderDetail.builder()
                .paymentType(paymentType)
                .paymentOption(paymentOption)
                .amount(amount)
                .build();
    }
}
