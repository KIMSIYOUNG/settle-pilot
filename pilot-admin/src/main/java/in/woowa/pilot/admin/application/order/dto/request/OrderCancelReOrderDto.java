package in.woowa.pilot.admin.application.order.dto.request;

import in.woowa.pilot.core.order.BusinessNo;
import in.woowa.pilot.core.order.Order;
import in.woowa.pilot.core.order.OrderDetail;
import in.woowa.pilot.core.order.OrderStatus;
import in.woowa.pilot.core.owner.Owner;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderCancelReOrderDto {
    private final String orderNo;
    private final List<OrderDetailDto> orderDetails;
    private final OrderStatus orderStatus;
    private final LocalDateTime orderDateTime;

    @Builder
    public OrderCancelReOrderDto(
            String orderNo,
            List<OrderDetailDto> orderDetails,
            OrderStatus orderStatus,
            LocalDateTime orderDateTime
    ) {
        this.orderNo = orderNo;
        this.orderDetails = orderDetails;
        this.orderStatus = orderStatus;
        this.orderDateTime = orderDateTime;
    }

    public Order toReOrder(Owner owner, BusinessNo businessNo) {
        Order order = Order.builder()
                .businessNo(businessNo)
                .owner(owner)
                .orderStatus(orderStatus)
                .orderDateTime(orderDateTime)
                .build();
        order.addOrderDetails(createOrderDetails());
        return order;
    }

    public List<OrderDetail> createOrderDetails() {
        return orderDetails.stream()
                .map(OrderDetailDto::toOrderDetail)
                .collect(Collectors.toList());
    }
}
