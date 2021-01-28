package in.woowa.pilot.core.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderPaymentAggregationSum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentOption paymentOption;

    @Column(nullable = false)
    private LocalDate criteriaDate;

    @Builder
    public OrderPaymentAggregationSum(
            PaymentType paymentType,
            PaymentOption paymentOption,
            BigDecimal totalAmount,
            LocalDate criteriaDate
    ) {
        this.paymentType = paymentType;
        this.paymentOption = paymentOption;
        this.totalAmount = totalAmount;
        this.criteriaDate = criteriaDate;
    }
}
