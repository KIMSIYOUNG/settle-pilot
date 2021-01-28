package in.woowa.pilot.core.order;

import in.woowa.pilot.core.common.BaseEntity;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.settle.Settle;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "orders", uniqueConstraints = @UniqueConstraint(name = "UNIQUE_ORDER_BUSINESS_NO", columnNames = {"orderStatus", "businessNo"}))
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private BusinessNo businessNo;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDateTime orderDateTime;

    @Embedded
    private OrderDetails orderDetails = new OrderDetails();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderSnapShot> orderSnapShots = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", foreignKey = @ForeignKey(name = "FK_OWNER_ORDER"))
    private Owner owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "settle_id", foreignKey = @ForeignKey(name = "FK_SETTLE_ORDER"))
    private Settle settle;

    @Builder
    public Order(
            BusinessNo businessNo,
            Owner owner,
            OrderStatus orderStatus,
            LocalDateTime orderDateTime
    ) {
        if (orderDateTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("주문시각은 현재시간 이전만 가능합니다.");
        }

        this.businessNo = businessNo;
        this.orderStatus = orderStatus;
        this.owner = owner;
        this.orderDateTime = orderDateTime;
        this.orderSnapShots.add(OrderSnapShot.builder()
                .order(this)
                .orderStatus(orderStatus)
                .statusAt(orderDateTime)
                .build());
    }

    @Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
    public Order(
            Long id,
            BusinessNo businessNo,
            Owner owner,
            OrderStatus orderStatus,
            LocalDateTime orderDateTime
    ) {
        if (orderDateTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("주문시각은 현재시간 이전만 가능합니다.");
        }

        this.id = id;
        this.businessNo = businessNo;
        this.orderStatus = orderStatus;
        this.owner = owner;
        this.orderDateTime = orderDateTime;
        this.orderSnapShots.add(OrderSnapShot.builder()
                .order(this)
                .orderStatus(orderStatus)
                .statusAt(orderDateTime)
                .build());
    }

    public void addOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = new OrderDetails(orderDetails, this);
        this.amount = this.orderDetails.createOrderAmount();
    }

    public void addOrderDetail(OrderDetail orderDetail) {
        orderDetails.addOrderDetail(orderDetail);
        this.amount = this.amount.add(orderDetail.getAmount());
    }

    public void updateStatus(OrderStatus status) {
        if (orderStatus.isConfirmed()) {
            throw new IllegalArgumentException("이미 주문완료/취소 된 주문입니다.");
        }

        this.orderStatus = status;
        this.orderSnapShots.add(OrderSnapShot.builder()
                .order(this)
                .orderStatus(status)
                .statusAt(LocalDateTime.now())
                .build());
    }

    public void cancel() {
        businessNo.cancel();
        this.orderStatus = OrderStatus.CANCEL;
        this.orderSnapShots.add(OrderSnapShot.builder()
                .order(this)
                .orderStatus(OrderStatus.CANCEL)
                .statusAt(LocalDateTime.now())
                .build());
    }

    public boolean isConfirmed() {
        return orderStatus.isConfirmed();
    }

    @Override
    public void delete() {
        if (isConfirmed()) {
            throw new IllegalArgumentException("이미 배달완료/취소 된 주문은 삭제할 수 없습니다.");
        }

        super.delete();
        orderDetails.deleteAll();
        orderSnapShots.forEach(BaseEntity::delete);
    }

    public String getBusinessNoValue() {
        if (businessNo == null) {
            return "";
        }

        return businessNo.getBusinessNo();
    }

    public List<OrderDetail> getOrderDetailValue() {
        return orderDetails.getOrderDetails();
    }
}
