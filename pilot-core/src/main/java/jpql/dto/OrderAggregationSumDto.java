package jpql.dto;

import in.woowa.pilot.core.order.OrderPaymentAggregationSum;
import in.woowa.pilot.core.order.PaymentOption;
import in.woowa.pilot.core.order.PaymentType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class OrderAggregationSumDto {
    private BigDecimal totalAmount;
    private PaymentType paymentType;
    private PaymentOption paymentOption;

    public OrderAggregationSumDto(BigDecimal totalAmount, PaymentType paymentType, PaymentOption paymentOption) {
        this.totalAmount = totalAmount;
        this.paymentType = paymentType;
        this.paymentOption = paymentOption;
    }

    public OrderPaymentAggregationSum toOrderPaymentAggregationSum(LocalDate criteriaDate) {
        return OrderPaymentAggregationSum.builder()
                .totalAmount(totalAmount)
                .paymentType(paymentType)
                .paymentOption(paymentOption)
                .criteriaDate(criteriaDate)
                .build();
    }
}
