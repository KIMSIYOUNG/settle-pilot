package in.woowa.pilot.fixture.order;

import in.woowa.pilot.core.order.OrderPaymentAggregation;
import in.woowa.pilot.core.order.OrderPaymentAggregationSum;
import in.woowa.pilot.core.order.PaymentOption;
import in.woowa.pilot.core.order.PaymentType;
import in.woowa.pilot.core.owner.Owner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class OrderPaymentAggregationFixture {

    public static OrderPaymentAggregation createWithoutId(Owner owner, PaymentType type, PaymentOption option, LocalDate date, BigDecimal money) {
        return OrderPaymentAggregation.builder()
                .paymentType(type)
                .paymentOption(option)
                .criteriaDate(date)
                .owner(owner)
                .totalAmount(money)
                .build();
    }

    public static OrderPaymentAggregationSum getPaymentAggregation(
            List<OrderPaymentAggregationSum> aggregations,
            PaymentType type,
            PaymentOption option
    ) {
        return aggregations.stream()
                .filter(ag -> ag.getPaymentType() == type)
                .filter(ag -> ag.getPaymentOption() == option)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static List<OrderPaymentAggregationSum> getPaymentAggregations(
            List<OrderPaymentAggregationSum> aggregations,
            PaymentType type,
            PaymentOption option
    ) {
        return aggregations.stream()
                .filter(ag -> ag.getPaymentType() == type)
                .filter(ag -> ag.getPaymentOption() == option)
                .collect(Collectors.toList());
    }
}
