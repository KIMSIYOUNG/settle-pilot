package in.woowa.pilot.core.order;

import in.woowa.pilot.core.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderSnapShot extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false, length = 30)
    private LocalDateTime statusAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "FK_ORDER_ORDER_SNAPSHOT"))
    private Order order;

    @Builder
    public OrderSnapShot(OrderStatus orderStatus, LocalDateTime statusAt, Order order) {
        this.orderStatus = orderStatus;
        this.statusAt = statusAt;
        this.order = order;
    }
}
