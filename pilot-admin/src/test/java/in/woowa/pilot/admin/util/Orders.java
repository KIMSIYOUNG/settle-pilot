package in.woowa.pilot.admin.util;

import in.woowa.pilot.admin.application.order.dto.request.OrderCreateDto;
import in.woowa.pilot.admin.application.order.dto.request.OrderDetailDto;
import in.woowa.pilot.admin.application.order.dto.response.OrderResponseDto;
import in.woowa.pilot.admin.application.order.dto.response.OrderResponsesDto;
import in.woowa.pilot.admin.common.CustomPageImpl;
import in.woowa.pilot.admin.presentation.order.dto.request.OrderCancelReOrderRequestDto;
import in.woowa.pilot.admin.presentation.order.dto.request.OrderCreateRequestDto;
import in.woowa.pilot.admin.presentation.order.dto.request.OrderDetailRequestDto;
import in.woowa.pilot.admin.presentation.order.dto.request.OrderStatusUpdateRequestDto;
import in.woowa.pilot.core.order.Order;
import in.woowa.pilot.core.order.OrderStatus;
import in.woowa.pilot.core.order.PaymentOption;
import in.woowa.pilot.core.order.PaymentType;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.fixture.BaseFixture;
import in.woowa.pilot.fixture.order.OrderFixture;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Orders {
    public static final OrderStatus ORDER_STATUS = OrderStatus.DELIVERY_CONFIRM;
    public static final String RESOURCE = "/api/orders";

    public static OrderCreateDto orderCreateRequest(Owner owner, int orderDetailCount, OrderStatus orderStatus) {
        return new OrderCreateDto(
                orderDetailRequestDto(orderDetailCount),
                owner.getId(),
                orderStatus,
                LocalDateTime.now()
        );
    }

    public static OrderCreateDto orderCreateRequest(Long ownerId, int orderDetailCount, LocalDateTime orderDateTime, OrderStatus orderStatus) {
        return new OrderCreateDto(
                orderDetailRequestDto(orderDetailCount),
                ownerId,
                orderStatus,
                orderDateTime
        );
    }

    public static OrderCreateRequestDto webOrderCreateRequest(Long ownerId, int orderDetailCount, LocalDateTime orderDateTime, OrderStatus orderStatus) {
        return new OrderCreateRequestDto(
                orderDetailWebRequestDto(orderDetailCount),
                ownerId,
                orderStatus,
                orderDateTime
        );
    }

    public static OrderCreateRequestDto webOrderCreateRequest(Long ownerId, int orderDetailCount, LocalDateTime orderDateTime) {
        return new OrderCreateRequestDto(
                orderDetailWebRequestDto(orderDetailCount),
                ownerId,
                ORDER_STATUS,
                orderDateTime
        );
    }

    public static List<OrderDetailDto> orderDetailRequestDto(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> new OrderDetailDto(
                        PaymentType.CARD,
                        PaymentOption.EMPTY,
                        BigDecimal.valueOf(10000)
                )).collect(Collectors.toList());
    }

    public static List<OrderDetailRequestDto> orderDetailWebRequestDto(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> new OrderDetailRequestDto(
                        PaymentType.CARD,
                        PaymentOption.EMPTY,
                        BigDecimal.valueOf(10000)
                )).collect(Collectors.toList());
    }

    public static List<OrderCreateDto> orderCreateRequests(Owner owner, int orderCount, LocalDateTime orderDateTime) {
        return IntStream.range(0, orderCount)
                .mapToObj(i -> orderCreateRequest(owner, 2, orderDateTime))
                .collect(Collectors.toList());
    }

    public static OrderCreateDto orderCreateRequest(Owner owner, int orderDetailCount, LocalDateTime orderDateTime) {
        return orderCreateRequest(owner.getId(), orderDetailCount, orderDateTime, ORDER_STATUS);
    }


    public static OrderResponsesDto createPagedResponsesDto(Owner owner) {
        List<Order> orders = IntStream.range(0, 2)
                .mapToObj(i -> OrderFixture.createWithId(owner))
                .collect(Collectors.toList());
        Page<Order> pagedOrders = new CustomPageImpl<>(orders, BaseFixture.DEFAULT_PAGEABLE, orders.size());

        return new OrderResponsesDto(pagedOrders);
    }


    public static OrderCreateDto orderCreateRequest(Owner owner, int orderDetailCount) {
        return orderCreateRequest(owner, orderDetailCount, ORDER_STATUS);
    }

    public static OrderCreateRequestDto webOrderCreateRequest(Owner owner, int orderDetailCount) {
        return OrderCreateRequestDto.testBuilder()
                .orderStatus(ORDER_STATUS)
                .ownerId(owner.getId())
                .orderDetails(orderDetailWebRequestDto(orderDetailCount))
                .orderDateTime(LocalDateTime.now())
                .build();
    }

    public static List<OrderCreateDto> orderCreateRequests(Owner owner, int orderCount, int orderDetailCount) {
        return IntStream.range(0, orderCount)
                .mapToObj(i -> orderCreateRequest(owner, orderDetailCount))
                .collect(Collectors.toList());
    }

    public static OrderCancelReOrderRequestDto webReOrderCreateRequest(int count) {
        return OrderCancelReOrderRequestDto.testBuilder()
                .orderNo("20201204abcdedfg")
                .orderDateTime(LocalDateTime.now())
                .orderStatus(OrderStatus.ORDER)
                .orderDetails(orderDetailWebRequestDto(count))
                .build();
    }

    public static in.woowa.pilot.admin.application.order.dto.request.OrderStatusUpdateDto orderStatusUpdateDto(Long id, OrderStatus orderStatus) {
        return new in.woowa.pilot.admin.application.order.dto.request.OrderStatusUpdateDto(id, orderStatus);
    }

    public static OrderStatusUpdateRequestDto webOrderStatusUpdateDto(Long id, OrderStatus orderStatus) {
        return new OrderStatusUpdateRequestDto(id, orderStatus);
    }

    public static OrderResponseDto createResponseDto(Owner owner) {
        return new OrderResponseDto(OrderFixture.createWithId(owner));
    }
}
