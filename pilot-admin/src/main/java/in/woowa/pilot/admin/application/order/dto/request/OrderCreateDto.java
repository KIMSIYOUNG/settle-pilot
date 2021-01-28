package in.woowa.pilot.admin.application.order.dto.request;

import in.woowa.pilot.core.order.BusinessNo;
import in.woowa.pilot.core.order.Order;
import in.woowa.pilot.core.order.OrderDetail;
import in.woowa.pilot.core.order.OrderStatus;
import in.woowa.pilot.core.owner.Owner;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class OrderCreateDto {
    private final List<OrderDetailDto> orderDetails;
    private final Long ownerId;
    private final OrderStatus orderStatus;
    private final LocalDateTime orderDateTime;

    @Builder
    public OrderCreateDto(
            List<OrderDetailDto> orderDetails,
            Long ownerId,
            OrderStatus orderStatus,
            LocalDateTime orderDateTime
    ) {
        this.orderDetails = orderDetails;
        this.ownerId = ownerId;
        this.orderStatus = orderStatus;
        this.orderDateTime = orderDateTime;
    }

    public Order toOrder(Owner owner) {
        Order order = Order.builder()
                .businessNo(new BusinessNo(LocalDate.now(), UUID.randomUUID().toString()))
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
