package in.woowa.pilot.fixture.order;

import in.woowa.pilot.core.order.BusinessNo;
import in.woowa.pilot.core.order.Order;
import in.woowa.pilot.core.order.OrderDetail;
import in.woowa.pilot.core.order.OrderStatus;
import in.woowa.pilot.core.order.PaymentOption;
import in.woowa.pilot.core.order.PaymentType;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.fixture.BaseFixture;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OrderFixture {
    public static final OrderStatus ORDER_STATUS = OrderStatus.DELIVERY_CONFIRM;
    public static final String RESOURCE = "/api/orders";

    public static Order createWithId(Owner owner) {
        Order order = Order.testBuilder()
                .id(2L)
                .owner(owner)
                .businessNo(getOrderNo())
                .orderStatus(ORDER_STATUS)
                .orderDateTime(LocalDateTime.now())
                .build();
        order.addOrderDetails(
                Arrays.asList(
                        OrderDetail.testBuilder()
                                .id(3L)
                                .paymentType(PaymentType.POINT)
                                .paymentOption(PaymentOption.EMPTY)
                                .amount(BigDecimal.valueOf(5000))
                                .build(),
                        OrderDetail.testBuilder()
                                .id(4L)
                                .paymentType(PaymentType.CARD)
                                .paymentOption(PaymentOption.EMPTY)
                                .amount(BigDecimal.valueOf(5000))
                                .build(),
                        OrderDetail.testBuilder()
                                .id(5L)
                                .paymentType(PaymentType.COUPON)
                                .paymentOption(PaymentOption.OWNER_COUPON)
                                .amount(BigDecimal.valueOf(2000))
                                .build()
                )
        );
        return order;
    }

    public static Order orderWithoutOrderDetail(Owner owner, List<OrderDetail> orderDetails, LocalDateTime orderDateTime) {
        Order order = Order.testBuilder()
                .businessNo(getOrderNo())
                .orderStatus(ORDER_STATUS)
                .orderDateTime(orderDateTime)
                .owner(owner)
                .build();
        order.addOrderDetails(orderDetails);
        return order;
    }

    public static Order orderWithoutOrderDetail(Owner owner, OrderDetail orderDetail, LocalDateTime orderDateTime) {
        Order order = Order.testBuilder()
                .businessNo(getOrderNo())
                .orderStatus(ORDER_STATUS)
                .orderDateTime(orderDateTime)
                .owner(owner)
                .build();
        order.addOrderDetails(Arrays.asList(orderDetail));
        return order;
    }

    public static Order orderWithoutOrderDetail(Owner owner, List<OrderDetail> orderDetails, OrderStatus orderStatus) {
        Order order = Order.testBuilder()
                .businessNo(getOrderNo())
                .orderStatus(orderStatus)
                .orderDateTime(LocalDateTime.now())
                .owner(owner)
                .build();
        order.addOrderDetails(orderDetails);
        return order;
    }

    public static List<OrderDetail> createOrderDetails(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> OrderDetail.testBuilder()
                        .paymentType(PaymentType.CARD)
                        .amount(BigDecimal.valueOf(10000))
                        .build())
                .collect(Collectors.toList());
    }

    public static OrderDetail createOrderDetails(PaymentType type, PaymentOption option) {
        return OrderDetail.testBuilder()
                .paymentType(type)
                .paymentOption(option)
                .amount(BigDecimal.valueOf(10000))
                .build();
    }

    public static Order createWithoutId(Owner owner, int orderDetailCount) {
        return orderWithoutOrderDetail(owner, createOrderDetails(orderDetailCount));
    }

    public static Order createWithoutId(Owner owner, PaymentType paymentType, PaymentOption paymentOption) {
        return orderWithoutOrderDetail(owner, orderDetail(paymentType, paymentOption));
    }

    public static Order createWithoutId(Owner owner, PaymentType paymentType, PaymentOption paymentOption, LocalDateTime orderDateTime) {
        return orderWithoutOrderDetail(owner, orderDetail(paymentType, paymentOption), orderDateTime);
    }

    public static OrderDetail orderDetail(PaymentType paymentType, PaymentOption paymentOption) {
        return OrderDetail.testBuilder()
                .amount(BigDecimal.valueOf(10_000))
                .paymentType(paymentType)
                .paymentOption(paymentOption)
                .build();
    }

    public static List<OrderDetail> createEveryPaymentType() {
        return Arrays.asList(
                orderDetail(PaymentType.CARD, PaymentOption.EMPTY),
                orderDetail(PaymentType.COUPON, PaymentOption.BAEMIN_COUPON),
                orderDetail(PaymentType.MOBILE, PaymentOption.EMPTY),
                orderDetail(PaymentType.POINT, PaymentOption.EMPTY)
        );
    }

    public static Order createWithoutId(Owner owner, int orderDetailCount, OrderStatus orderStatus) {
        return orderWithoutOrderDetail(owner, createOrderDetails(orderDetailCount), orderStatus);
    }

    public static Order createWithoutId(Owner owner, int orderDetailCount, LocalDateTime orderDateTime) {
        return orderWithoutOrderDetail(owner, createOrderDetails(orderDetailCount), orderDateTime);
    }

    public static Order orderWithoutOrderDetail(Owner owner, List<OrderDetail> orderDetails) {
        return orderWithoutOrderDetail(owner, orderDetails, BaseFixture.YESTERDAY_MIDNIGHT);
    }

    public static Order orderWithoutOrderDetail(Owner owner, OrderDetail orderDetail) {
        return orderWithoutOrderDetail(owner, orderDetail, BaseFixture.YESTERDAY_MIDNIGHT);
    }

    public static BusinessNo getOrderNo() {
        return new BusinessNo(LocalDate.now(), UUID.randomUUID().toString());
    }
}
