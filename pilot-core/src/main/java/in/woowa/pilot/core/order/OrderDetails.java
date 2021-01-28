package in.woowa.pilot.core.order;

import in.woowa.pilot.core.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class OrderDetails {

    @Column(nullable = false)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public OrderDetails(List<OrderDetail> orderDetails, Order order) {
        if (CollectionUtils.isEmpty(orderDetails)) {
            throw new IllegalArgumentException("주문 상세정보를 입력해주세요.(결제수단)");
        }

        this.orderDetails = orderDetails;
        this.orderDetails.forEach((od) -> od.setOrder(order));
    }

    public void addOrderDetail(OrderDetail orderDetail) {
        Objects.requireNonNull(orderDetail, "주문 상세를 입력해주세요.");

        orderDetails.add(orderDetail);
    }

    public void deleteAll() {
        orderDetails.forEach(BaseEntity::delete);
    }

    public BigDecimal createOrderAmount() {
        return orderDetails.stream()
                .map(OrderDetail::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
