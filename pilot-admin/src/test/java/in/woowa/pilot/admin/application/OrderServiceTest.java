package in.woowa.pilot.admin.application;

import com.querydsl.core.NonUniqueResultException;
import in.woowa.pilot.admin.application.order.OrderService;
import in.woowa.pilot.admin.application.order.dto.request.OrderCancelReOrderDto;
import in.woowa.pilot.admin.application.order.dto.request.OrderCreateDto;
import in.woowa.pilot.admin.application.order.dto.request.OrderSearchDto;
import in.woowa.pilot.admin.application.order.dto.response.OrderResponseDto;
import in.woowa.pilot.admin.application.settle.SettleService;
import in.woowa.pilot.admin.application.settle.dto.request.SettleInCaseCreateDto;
import in.woowa.pilot.admin.application.settle.dto.response.SettleResponseDto;
import in.woowa.pilot.admin.common.exception.ResourceNotFoundException;
import in.woowa.pilot.admin.util.Orders;
import in.woowa.pilot.core.order.OrderStatus;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.owner.OwnerRepository;
import in.woowa.pilot.core.settle.SettleType;
import in.woowa.pilot.fixture.BaseFixture;
import in.woowa.pilot.fixture.owner.OwnerFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static in.woowa.pilot.fixture.TestUtils.assertThatBigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderServiceTest extends IntegrationTest {
    private Owner owner;

    @Autowired
    OrderService orderService;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    SettleService settleService;

    @BeforeEach
    void setUp() {
        owner = ownerRepository.save(OwnerFixture.createWithoutId());
    }

    @DisplayName("연관관계인 Owner가 존재하지 않으면 예외를 반환한다.")
    @Test
    void createWithoutOwner() {
        // given
        Owner ownerWithoutId = OwnerFixture.createWithoutId();
        OrderCreateDto request = Orders.orderCreateRequest(ownerWithoutId, 2);
        // when, then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @DisplayName("주문을 정상적으로 생성한다.")
    @Test
    void create() {
        // given
        OrderCreateDto request = Orders.orderCreateRequest(owner, 2);
        // when
        OrderResponseDto orderResponse = orderService.create(request);
        // then
        assertAll(
                () -> assertThat(orderResponse.getId()).isNotNull(),
                () -> assertThat(orderResponse.getOwner().getId()).isEqualTo(owner.getId()),
                () -> assertThat(orderResponse.getOrderDetails().getOrderDetails()).hasSize(2),
                () -> assertThat(orderResponse.getOrderSnapShots().getSnapshots()).hasSize(1)
        );
    }

    @DisplayName("주문을 취소하고 재등록한다.")
    @Test
    void cancelAndReOrder() {
        // given
        OrderResponseDto order = orderService.create(Orders.orderCreateRequest(owner, 2, OrderStatus.ORDER));
        // when
        OrderCancelReOrderDto request = new OrderCancelReOrderDto(
                order.getOrderNo(),
                Orders.orderDetailRequestDto(2),
                OrderStatus.ORDER_CONFIRM,
                LocalDateTime.now()
        );
        OrderResponseDto reOrder = orderService.cancelAndReOrder(request);
        OrderResponseDto origin = orderService.fetchById(order.getId());
        // then
        assertAll(
                () -> assertThat(origin.getOrderNo()).contains(reOrder.getOrderNo()),
                () -> assertThat(origin.getOrderNo()).contains("CANCEL"),
                () -> assertThat(origin.getOrderStatus()).isEqualTo(OrderStatus.CANCEL.name()),
                () -> assertThat(origin.getOwner().getId()).isEqualTo(reOrder.getOwner().getId())
        );
    }

    @DisplayName("재등록한 주문은 재취소,재등록이 불가능하다. 취소/등록을 따로 해야한다.")
    @Test
    void cancelOrder() {
        // given
        OrderResponseDto order = orderService.create(Orders.orderCreateRequest(owner, 2, OrderStatus.ORDER));
        OrderCancelReOrderDto request1 = new OrderCancelReOrderDto(
                order.getOrderNo(),
                Orders.orderDetailRequestDto(2),
                OrderStatus.ORDER_CONFIRM,
                LocalDateTime.now()
        );
        OrderResponseDto reOrder = orderService.cancelAndReOrder(request1);
        // when
        OrderCancelReOrderDto request2 = OrderCancelReOrderDto.builder()
                .orderNo(reOrder.getOrderNo())
                .orderDateTime(reOrder.getOrderDateTime())
                .orderDetails(Orders.orderDetailRequestDto(2))
                .orderStatus(OrderStatus.ORDER_CONFIRM)
                .build();

        assertThatThrownBy(() -> orderService.cancelAndReOrder(request2))
                .isInstanceOf(NonUniqueResultException.class);
    }

    @DisplayName("연관관계에 있는 모든 엔티티를 함께 조회한다.(주문-주문상세-업주)")
    @Test
    void fetchById() {
        // given
        OrderCreateDto request = Orders.orderCreateRequest(owner, 2);
        OrderResponseDto expected = orderService.create(request);
        // when
        OrderResponseDto actual = orderService.fetchById(expected.getId());
        // then
        assertAll(
                () -> assertThat(actual).isEqualToIgnoringGivenFields(expected, "owner", "orderDetails", "amount", "orderSnapShots"),
                () -> assertThat(actual.getOrderDetails().getOrderDetails()).hasSize(2),
                () -> assertThat(actual.getOrderSnapShots().getSnapshots()).hasSize(1),
                () -> assertThat(actual.getOwner().getId()).isEqualToComparingFieldByField(expected.getOwner().getId()),
                () -> assertThatBigDecimal(actual.getAmount()).isEqualTo(expected.getAmount())
        );
    }

    @DisplayName("업주 ID를 기준으로 주문을 조회한다." +
            "저장된 주문 7개 | 첫 번째 업주기준(6개) 조회 | 3페이지 2개 |")
    @Test
    void fetchByOwnerId() {
        // given
        Owner first = ownerRepository.save(OwnerFixture.createWithoutId("OWNER1", "owner@owner.com"));
        Owner second = ownerRepository.save(OwnerFixture.createWithoutId("OWNER2", "owner2@owner.com"));

        saveAll(
                Orders.orderCreateRequests(first, 6, 2),
                Orders.orderCreateRequests(second, 1, 2)
        );
        // when
        OrderSearchDto condition = OrderSearchDto.builder()
                .ownerId(first.getId())
                .build();
        Page<OrderResponseDto> findOrders = orderService.fetchPagedByCondition(condition, PageRequest.of(2, 2)).getOrders();
        // then
        assertAll(
                () -> assertThat(findOrders.getTotalElements()).isEqualTo(6),
                () -> assertThat(findOrders.getPageable().getPageSize()).isEqualTo(2),
                () -> assertThat(findOrders.getPageable().getPageNumber()).isEqualTo(2),
                () -> assertThat(findOrders.getPageable().getOffset()).isEqualTo(4)
        );
    }

    @DisplayName("오늘 현재까지 발생한 주문을 조회한다." +
            "총 주문 14건 오늘자정 5 어제자정 3 1페이지부터 2개씩 확인")
    @Test
    void fetchTodayOrders() {
        // given
        saveAll(
                Orders.orderCreateRequests(owner, 5, BaseFixture.TODAY_MIDNIGHT),
                Orders.orderCreateRequests(owner, 3, BaseFixture.YESTERDAY_MIDNIGHT)
        );
        OrderSearchDto condition = OrderSearchDto.builder()
                .startAt(BaseFixture.TODAY_MIDNIGHT)
                .endAt(BaseFixture.NOW)
                .build();
        // when
        Page<OrderResponseDto> findOrders = orderService.fetchPagedByCondition(condition, PageRequest.of(0, 2)).getOrders();
        // then
        assertAll(
                () -> assertThat(findOrders.getTotalElements()).isEqualTo(5),
                () -> assertThat(findOrders.getTotalPages()).isEqualTo(3),
                () -> assertThat(findOrders.getContent()).hasSize(2)
        );
    }

    @DisplayName("주문 데이터 - 4일전 현시각 ~ 오늘 3시간전까지 총 9건" +
            "조회 조건 - 3일전 현시각 ~ 오늘 4시간전까지 = 총 3건 예상")
    @Test
    void fetchFromTo() {
        // given
        saveAll(
                Orders.orderCreateRequests(owner, 3, LocalDateTime.now().minusDays(4)),
                Orders.orderCreateRequests(owner, 3, LocalDateTime.now().minusDays(2)),
                Orders.orderCreateRequests(owner, 3, LocalDateTime.now().minusHours(3))
        );
        // when
        OrderSearchDto condition = OrderSearchDto.builder()
                .startAt(LocalDateTime.now().minusDays(3))
                .endAt(LocalDateTime.now().minusHours(4))
                .build();
        Page<OrderResponseDto> findOrders = orderService.fetchPagedByCondition(condition, PageRequest.of(0, 4)).getOrders();
        // then
        assertAll(
                () -> assertThat(findOrders.getTotalElements()).isEqualTo(3),
                () -> assertThat(findOrders.getTotalPages()).isEqualTo(1),
                () -> assertThat(findOrders.getContent()).hasSize(3)
        );
    }

    @DisplayName("주문상태를 변경한다. 주문 -> 배달중")
    @Test
    void updateOrderStatus() {
        // given
        OrderResponseDto origin = orderService.create(Orders.orderCreateRequest(owner, 1, OrderStatus.ORDER));
        // when
        orderService.updateOrderStatus(Orders.orderStatusUpdateDto(origin.getId(), OrderStatus.DELIVERY));
        OrderResponseDto updatedResponse = orderService.fetchById(origin.getId());
        // then
        assertAll(
                () -> assertThat(origin.getOrderStatus()).isEqualTo(OrderStatus.ORDER.name()),
                () -> assertThat(updatedResponse.getOrderStatus()).isEqualTo(OrderStatus.DELIVERY.name())
        );
    }

    @DisplayName("정산 ID로 주문을 검색할 수 있다.")
    @Test
    void fetchBySettleId() {
        // given
        OrderResponseDto order1 = orderService.create(Orders.orderCreateRequest(owner, 1, LocalDateTime.now().minusDays(5)));
        OrderResponseDto order2 = orderService.create(Orders.orderCreateRequest(owner, 2, LocalDateTime.now()));
        // when
        SettleInCaseCreateDto settleRequest = new SettleInCaseCreateDto(owner.getId(), SettleType.DAILY, LocalDate.now().minusDays(5));
        SettleResponseDto settle = settleService.createInCaseSettle(settleRequest);
        Page<OrderResponseDto> orders = orderService.fetchBySettleId(settle.getId(), BaseFixture.DEFAULT_PAGEABLE).getOrders();
        OrderResponseDto findOrder = orders.getContent().get(0);
        // then
        assertAll(
                () -> assertThat(orders).hasSize(1),
                () -> assertThat(findOrder).isEqualToIgnoringGivenFields(order1, "orderDetails", "orderSnapShots", "amount", "owner"),
                () -> assertThatBigDecimal(findOrder.getAmount()).isEqualTo(order1.getAmount()),
                () -> assertThat(findOrder.getOwner().getId()).isEqualTo(order1.getOwner().getId())
        );
    }

    @DisplayName("주문을 정상적으로 삭제한다. 주문 삭제시, 주문상세도 함께 삭제된다.")
    @Test
    void delete() {
        // given
        OrderResponseDto response = orderService.create(
                Orders.orderCreateRequest(owner, 3, OrderStatus.ORDER));
        orderService.create(Orders.orderCreateRequest(owner, 3));
        // when
        orderService.delete(response.getId());
        // then
        assertThatThrownBy(() -> orderService.fetchById(response.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("order not found with id : '%s'", response.getId()));
    }

    private void saveAll(List<OrderCreateDto>... request) {
        for (List<OrderCreateDto> requestDtos : request) {
            requestDtos.forEach(o -> orderService.create(o));
        }
    }
}