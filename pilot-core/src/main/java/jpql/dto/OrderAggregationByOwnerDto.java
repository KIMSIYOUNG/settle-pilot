package jpql.dto;

import in.woowa.pilot.core.order.OrderPaymentAggregation;
import in.woowa.pilot.core.order.PaymentOption;
import in.woowa.pilot.core.order.PaymentType;
import in.woowa.pilot.core.owner.Owner;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class OrderAggregationByOwnerDto {

    private BigDecimal totalAmount;
    private PaymentType paymentType;
    private PaymentOption paymentOption;
    private Owner owner;

    public OrderAggregationByOwnerDto(
            Owner owner,
            PaymentType paymentType,
            PaymentOption paymentOption,
            BigDecimal totalAmount
    ) {
        this.totalAmount = totalAmount;
        this.paymentType = paymentType;
        this.paymentOption = paymentOption;
        this.owner = owner;
    }

    public OrderPaymentAggregation toOrderPaymentAggregation(LocalDate criteriaDate) {
        return OrderPaymentAggregation.builder()
                .totalAmount(totalAmount)
                .paymentType(paymentType)
                .paymentOption(paymentOption)
                .owner(owner)
                .criteriaDate(criteriaDate)
                .build();
    }
}
