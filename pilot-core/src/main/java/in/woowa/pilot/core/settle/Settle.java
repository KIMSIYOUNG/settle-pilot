package in.woowa.pilot.core.settle;

import in.woowa.pilot.core.common.BaseEntity;
import in.woowa.pilot.core.order.BusinessNo;
import in.woowa.pilot.core.order.Order;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.reward.Reward;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "settle", uniqueConstraints = @UniqueConstraint(name = "UNIQUE_SETTLE_BUSINESS_NO", columnNames = {"businessNo"}))
public class Settle extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private BusinessNo businessNo;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private SettleStatus settleStatus;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private SettleType settleType;

    @Column(nullable = false)
    private LocalDateTime transactionStartAt;

    @Column(nullable = false)
    private LocalDateTime transactionEndAt;

    @Column(nullable = false)
    private LocalDateTime settleScheduleDate;

    private LocalDateTime settleCompleteDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", foreignKey = @ForeignKey(name = "FK_OWNER_SETTLE"))
    private Owner owner;

    @Builder
    public Settle(
            Owner owner,
            BusinessNo businessNo,
            List<Order> orders,
            List<Reward> rewards,
            SettleType settleType,
            LocalDateTime transactionStartAt,
            LocalDateTime transactionEndAt,
            LocalDateTime settleScheduleDate
    ) {
        Objects.requireNonNull(orders, "지급금 대상 주문은 필수항목입니다.");
        Objects.requireNonNull(rewards, "지급금 대상 보상금액은 필수항목입니다.");

        if (transactionStartAt.isAfter(transactionEndAt)) {
            throw new IllegalArgumentException("거래시작일은 종료일보다 이전일 수 없습니다.");
        }

        this.businessNo = businessNo;
        this.amount = calculate(orders, rewards);
        this.owner = owner;
        this.transactionStartAt = transactionStartAt;
        this.settleStatus = SettleStatus.CREATED;
        this.settleType = settleType;
        this.transactionEndAt = transactionEndAt;
        this.settleScheduleDate = settleScheduleDate;
    }

    @Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
    public Settle(
            Long id,
            BusinessNo businessNo,
            Owner owner,
            List<Order> orders,
            List<Reward> rewards,
            SettleType settleType,
            LocalDateTime transactionStartAt,
            LocalDateTime transactionEndAt,
            LocalDateTime settleScheduleDate
    ) {
        Objects.requireNonNull(orders, "지급금 대상 주문은 필수항목입니다.");
        Objects.requireNonNull(rewards, "지급금 대상 보상금액은 필수항목입니다.");

        if (transactionStartAt.isAfter(transactionEndAt)) {
            throw new IllegalArgumentException("거래시작일은 종료일보다 이전일 수 없습니다.");
        }

        this.id = id;
        this.businessNo = businessNo;
        this.amount = calculate(orders, rewards);
        this.owner = owner;
        this.transactionStartAt = transactionStartAt;
        this.settleStatus = SettleStatus.CREATED;
        this.settleType = settleType;
        this.transactionEndAt = transactionEndAt;
        this.settleScheduleDate = settleScheduleDate;
    }

    public BigDecimal calculate(List<Order> orders, List<Reward> rewards) {
        BigDecimal ordersAmount = orders.stream()
                .filter(Order::isConfirmed)
                .map(Order::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal rewardsAmount = rewards.stream()
                .map(Reward::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return ordersAmount.add(rewardsAmount);
    }

    public boolean isCompleted() {
        return this.settleStatus.isCompleted();
    }

    public void changeStatus(SettleStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("지급상태는 null이어서는 안됩니다.");
        }

        if (this.settleStatus == SettleStatus.COMPLETED) {
            throw new IllegalArgumentException("이미 완료된 지급금입니다.");
        }

        this.settleStatus = status;
        this.settleCompleteDate = LocalDateTime.now();
    }

    public String getBusinessNoValue() {
        if (businessNo == null) {
            return "";
        }

        return businessNo.getBusinessNo();
    }
}
