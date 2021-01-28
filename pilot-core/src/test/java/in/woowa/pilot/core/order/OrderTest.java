package in.woowa.pilot.core.order;

import in.woowa.pilot.core.common.Status;
import in.woowa.pilot.fixture.order.OrderFixture;
import in.woowa.pilot.fixture.owner.OwnerFixture;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OrderTest {
    public static LocalDateTime YESTERDAY = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIDNIGHT);

    @DisplayName("비즈니스 번호를 입력하지 않아도 NPE가 발생하지 않는다. - 빈 문자열 반환")
    @Test
    void createWithoutBusinessNo() {
        // given
        OrderDetail orderDetail = OrderDetail.builder()
                .paymentType(PaymentType.COUPON)
                .paymentOption(PaymentOption.OWNER_COUPON)
                .amount(BigDecimal.valueOf(3000))
                .build();
        // when
        Order order = Order.builder()
                .businessNo(null)
                .orderDateTime(YESTERDAY)
                .owner(OwnerFixture.createWithoutId())
                .orderStatus(OrderStatus.ORDER)
                .build();
        order.addOrderDetail(orderDetail);
        // when & then
        assertThat(order.getBusinessNoValue()).isEqualTo("");
    }

    @DisplayName("주문 시각은 현재시간 이전만 가능하다.")
    @Test
    void createFutureOrder() {
        assertThatThrownBy(() -> Order.builder()
                .orderStatus(OrderStatus.ORDER)
                .businessNo(orderNo())
                .orderDateTime(LocalDateTime.now().plusMinutes(1))
                .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문시각은 현재시간 이전만 가능합니다.");
    }

    @DisplayName("주문상세가 빈 경우 예외를 반환한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void nullOrderDetails(List<OrderDetail> orderDetails) {
        Order order = Order.builder()
                .businessNo(orderNo())
                .orderDateTime(LocalDateTime.now())
                .build();

        assertThatThrownBy(() -> order.addOrderDetails(orderDetails))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상세정보를 입력해주세요.(결제수단)");
    }

    @DisplayName("주문도메인을 생성할 때 결제수단에 따른 금액도 함께 계산된다.")
    @Test
    void amount() {
        // given
        OrderDetail orderDetail = OrderDetail.builder()
                .paymentType(PaymentType.COUPON)
                .paymentOption(PaymentOption.OWNER_COUPON)
                .amount(BigDecimal.valueOf(3000))
                .build();

        OrderDetail orderDetail2 = OrderDetail.builder()
                .paymentType(PaymentType.CARD)
                .amount(BigDecimal.valueOf(13000))
                .build();
        // when
        Order order = Order.builder()
                .businessNo(orderNo())
                .orderDateTime(YESTERDAY)
                .owner(OwnerFixture.createWithoutId())
                .orderStatus(OrderStatus.ORDER)
                .build();

        order.addOrderDetail(orderDetail);
        order.addOrderDetail(orderDetail2);
        // then
        assertThat(order.getAmount()).isEqualTo(BigDecimal.valueOf(10000));
    }

    @DisplayName("배달완료 되었거나, 주문취소 된 경우 상태를 변경할 수 없다.")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"DELIVERY_CONFIRM", "CANCEL"})
    void updateFail(OrderStatus orderStatus) {
        // given
        Order order = Order.builder()
                .businessNo(orderNo())
                .orderDateTime(YESTERDAY)
                .owner(OwnerFixture.createWithoutId())
                .orderStatus(orderStatus)
                .build();
        order.addOrderDetails(OrderFixture.createOrderDetails(2));
        // when , when
        assertThatThrownBy(() -> order.updateStatus(OrderStatus.DELIVERY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 주문완료/취소 된 주문입니다.");
    }

    @DisplayName("정상적으로 주문상태를 변경할 수 있다.")
    @Test
    void update() {
        // given
        Order order = Order.builder()
                .businessNo(orderNo())
                .orderDateTime(YESTERDAY)
                .owner(OwnerFixture.createWithoutId())
                .orderStatus(OrderStatus.ORDER)
                .build();
        order.addOrderDetails(OrderFixture.createOrderDetails(2));
        // when
        order.updateStatus(OrderStatus.ORDER_CONFIRM);
        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ORDER_CONFIRM);
        assertThat(order.getOrderSnapShots().get(1).getOrderStatus()).isEqualTo(OrderStatus.ORDER_CONFIRM);
    }

    @DisplayName("주문을 취소할 수 있다.")
    @Test
    void reOrder() {
        // given
        Order order = Order.builder()
                .businessNo(orderNo())
                .orderDateTime(YESTERDAY)
                .owner(OwnerFixture.createWithoutId())
                .orderStatus(OrderStatus.ORDER)
                .build();
        order.addOrderDetails(OrderFixture.createOrderDetails(2));
        // when
        order.cancel();
        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(order.getBusinessNoValue().contains(OrderStatus.CANCEL.name())).isTrue();
    }

    @DisplayName("한번 취소된 주문은 재취소할 수 없다.")
    @Test
    void reorderTwice() {
        // given
        Order order = Order.builder()
                .businessNo(orderNo())
                .orderDateTime(YESTERDAY)
                .owner(OwnerFixture.createWithoutId())
                .orderStatus(OrderStatus.ORDER)
                .build();
        // when
        order.cancel();
        // then
        assertThatThrownBy(order::cancel)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 취소된 주문입니다.");
    }

    @DisplayName("주문을 삭제하면 주문상세/스냅샷도 삭제된다.")
    @Test
    void deleteOrderAndOrderDetails() {
        // given
        Order order = Order.builder()
                .businessNo(orderNo())
                .orderDateTime(YESTERDAY)
                .owner(OwnerFixture.createWithoutId())
                .orderStatus(OrderStatus.ORDER)
                .build();
        order.addOrderDetails(OrderFixture.createOrderDetails(3));
        // when
        order.delete();
        List<OrderDetail> orderDetails = order.getOrderDetails().getOrderDetails();
        // then
        assertAll(
                () -> assertThat(order.getStatus()).isEqualTo(Status.DELETED),
                () -> assertThat(order.getDeletedAt()).isCloseTo(LocalDateTime.now(), new TemporalUnitWithinOffset(1L, ChronoUnit.SECONDS)),
                () -> assertThat(orderDetails.stream().anyMatch(od -> od.getStatus() != Status.DELETED)).isFalse(),
                () -> assertThat(order.getOrderSnapShots().stream().anyMatch(od -> od.getStatus() != Status.DELETED)).isFalse()
        );
    }

    @DisplayName("주문완료/취소 된 주문은 삭제가 불가능하다.")
    @Test
    void delete() {
        // given
        Order deliveryConfirmed = Order.builder()
                .businessNo(orderNo())
                .orderDateTime(YESTERDAY)
                .owner(OwnerFixture.createWithoutId())
                .orderStatus(OrderStatus.DELIVERY_CONFIRM)
                .build();

        Order orderCanceled = Order.builder()
                .businessNo(orderNo())
                .orderDateTime(YESTERDAY)
                .owner(OwnerFixture.createWithoutId())
                .orderStatus(OrderStatus.CANCEL)
                .build();
        // when , then
        assertAll(
                () -> assertThatThrownBy(deliveryConfirmed::delete)
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("이미 배달완료/취소 된 주문은 삭제할 수 없습니다."),
                () -> assertThatThrownBy(orderCanceled::delete)
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("이미 배달완료/취소 된 주문은 삭제할 수 없습니다.")
        );
    }

    private BusinessNo orderNo() {
        return new BusinessNo(LocalDate.now(), UUID.randomUUID().toString());
    }
}
