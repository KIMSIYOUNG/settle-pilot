package in.woowa.pilot.batch.repository;

import in.woowa.pilot.core.order.Order;
import in.woowa.pilot.core.order.OrderRepository;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.owner.OwnerRepository;
import in.woowa.pilot.core.settle.Settle;
import in.woowa.pilot.core.settle.SettleRepository;
import in.woowa.pilot.core.settle.SettleType;
import in.woowa.pilot.fixture.IntegrationTest;
import in.woowa.pilot.fixture.order.OrderFixture;
import in.woowa.pilot.fixture.owner.OwnerFixture;
import in.woowa.pilot.fixture.settle.SettleFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderCustomRepositoryTest extends IntegrationTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    OrderCustomRepository orderCustomRepository;

    @Autowired
    OrderBatchRepository orderBatchRepository;

    @Autowired
    SettleRepository settleRepository;

    @DisplayName("지급되지 않은 주문들을 업주기준으로 조회할 수 있다.")
    @Test
    void findUnSettledOrderByOwner() {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.DAILY));
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.DAILY));

        Order order1 = orderRepository.save(OrderFixture.createWithoutId(owner, 1, LocalDateTime.now()));
        Order order2 = orderRepository.save(OrderFixture.createWithoutId(owner, 1, LocalDateTime.now()));
        Order order3 = orderRepository.save(OrderFixture.createWithoutId(owner, 1, LocalDateTime.now()));
        orderRepository.save(OrderFixture.createWithoutId(owner, 1, LocalDateTime.now().minusDays(1)));
        orderRepository.save(OrderFixture.createWithoutId(owner1, 1, LocalDateTime.now()));

        Settle settle = settleRepository.save(SettleFixture.createWithoutId(owner));
        orderBatchRepository.updateBatchSettle(Arrays.asList(order3), settle);
        // when
        List<Order> orders = orderCustomRepository.findUnSettledOrderByOwner(owner, SettleType.DAILY, LocalDate.now());
        // then
        assertAll(
                () -> assertThat(orders).hasSize(2),
                () -> assertThat(orders.get(0).getId()).isEqualTo(order1.getId()),
                () -> assertThat(orders.get(0).getSettle()).isNull(),
                () -> assertThat(orders.get(1).getId()).isEqualTo(order2.getId()),
                () -> assertThat(orders.get(1).getSettle()).isNull()
        );
    }
}