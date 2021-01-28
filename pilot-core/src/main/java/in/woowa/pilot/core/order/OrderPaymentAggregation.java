package in.woowa.pilot.core.order;

import in.woowa.pilot.core.common.BaseEntity;
import in.woowa.pilot.core.owner.Owner;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderPaymentAggregation extends BaseEntity {

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Owner owner;

    @Builder
    public OrderPaymentAggregation(
            PaymentType paymentType,
            PaymentOption paymentOption,
            BigDecimal totalAmount,
            LocalDate criteriaDate,
            Owner owner
    ) {
        this.paymentType = paymentType;
        this.paymentOption = paymentOption;
        this.totalAmount = totalAmount;
        this.criteriaDate = criteriaDate;
        this.owner = owner;
    }
}
