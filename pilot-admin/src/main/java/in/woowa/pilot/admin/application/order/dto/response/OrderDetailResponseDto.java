package in.woowa.pilot.admin.application.order.dto.response;

import in.woowa.pilot.core.order.OrderDetail;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class OrderDetailResponseDto {
    private String paymentType;
    private String paymentOption;
    private BigDecimal amount;

    public OrderDetailResponseDto(OrderDetail orderDetail) {
        this.paymentType = orderDetail.getPaymentType().name();
        this.paymentOption = orderDetail.getPaymentOption() == null ? null : orderDetail.getPaymentOption().name();
        this.amount = orderDetail.getAmount();
    }
}
