package in.woowa.pilot.core.order;

import in.woowa.pilot.core.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "FK_ORDER_ORDER_DETAIL"))
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private PaymentOption paymentOption;

    @Column(nullable = false)
    private BigDecimal amount;

    @Builder
    public OrderDetail(PaymentType paymentType, PaymentOption paymentOption, BigDecimal amount) {
        this.paymentType = paymentType;
        this.paymentOption = paymentOption;
        this.amount = calculateAmount(amount);
    }

    @Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
    public OrderDetail(Long id, PaymentType paymentType, PaymentOption paymentOption, BigDecimal amount) {
        this.id = id;
        this.paymentType = paymentType;
        this.paymentOption = paymentOption;
        this.amount = calculateAmount(amount);
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    private BigDecimal calculateAmount(BigDecimal amount) {
        BigDecimal discountRate = paymentType.calculate(paymentOption);

        return amount.multiply(discountRate);
    }

    public PaymentOption getPaymentOption() {
        if (paymentOption == null) {
            return PaymentOption.EMPTY;
        }
        return paymentOption;
    }
}
