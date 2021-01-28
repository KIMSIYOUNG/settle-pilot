package in.woowa.pilot.core.order;

import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.owner.OwnerRepository;
import in.woowa.pilot.fixture.RepositoryTest;
import in.woowa.pilot.fixture.order.OrderFixture;
import in.woowa.pilot.fixture.owner.OwnerFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderRepositoryTest extends RepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @DisplayName("삭제된 주문은 조회되지 않는다.")
    @Test
    void findActiveById() {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());
        Order order = orderRepository.save(OrderFixture.createWithoutId(owner, 1, OrderStatus.ORDER));
        // when
        order.delete();
        // then
        Optional<Order> findOrder = orderRepository.findById(order.getId());

        assertThat(findOrder.isPresent()).isFalse();
    }

    @DisplayName("전체조회에서도 삭제된 주문은 조회되지 않는다.")
    @Test
    void findActiveAllBy() {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());
        Order order1 = orderRepository.save(OrderFixture.createWithoutId(owner, 1, OrderStatus.ORDER));
        Order order2 = orderRepository.save(OrderFixture.createWithoutId(owner, 1, OrderStatus.ORDER));
        Order order3 = orderRepository.save(OrderFixture.createWithoutId(owner, 1, OrderStatus.ORDER));
        // when
        order1.delete();
        order2.delete();
        // then
        List<Order> orders = orderRepository.findAll();

        assertAll(
                () -> assertThat(orders).hasSize(1),
                () -> assertThat(orders.get(0).getId()).isEqualTo(order3.getId())
        );
    }
}