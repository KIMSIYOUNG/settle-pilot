package in.woowa.pilot.batch.repository;

import in.woowa.pilot.core.order.Order;
import in.woowa.pilot.core.order.OrderRepository;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.owner.OwnerRepository;
import in.woowa.pilot.core.settle.Settle;
import in.woowa.pilot.core.settle.SettleRepository;
import in.woowa.pilot.fixture.IntegrationTest;
import in.woowa.pilot.fixture.order.OrderFixture;
import in.woowa.pilot.fixture.owner.OwnerFixture;
import in.woowa.pilot.fixture.settle.SettleFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderBatchRepositoryTest extends IntegrationTest {

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    SettleRepository settleRepository;

    @Autowired
    OrderBatchRepository orderBatchRepository;

    @DisplayName("특정 지급금 번호로 업데이트 시킬 수 있다.")
    @Test
    void update() {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());

        List<Order> orders = orderRepository.saveAll(Arrays.asList(
                OrderFixture.createWithoutId(owner, 1),
                OrderFixture.createWithoutId(owner, 1),
                OrderFixture.createWithoutId(owner, 1)
        ));
        Settle settle = settleRepository.save(SettleFixture.createWithoutId(owner));
        // when
        long count = orderBatchRepository.updateBatchSettle(orders, settle);
        // then
        assertThat(count).isEqualTo(3);
    }
}