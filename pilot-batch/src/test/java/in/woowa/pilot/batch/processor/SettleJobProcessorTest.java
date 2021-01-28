package in.woowa.pilot.batch.processor;

import in.woowa.pilot.batch.dto.SettleJobDto;
import in.woowa.pilot.batch.parameter.SettleParameter;
import in.woowa.pilot.core.order.OrderRepository;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.owner.OwnerRepository;
import in.woowa.pilot.core.reward.RewardRepository;
import in.woowa.pilot.core.settle.SettleType;
import in.woowa.pilot.fixture.IntegrationTest;
import in.woowa.pilot.fixture.order.OrderFixture;
import in.woowa.pilot.fixture.owner.OwnerFixture;
import in.woowa.pilot.fixture.reward.RewardFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

class SettleJobProcessorTest extends IntegrationTest {

    @Autowired
    SettleJobProcessor jobProcessor;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    RewardRepository rewardRepository;

    @MockBean
    SettleParameter settleParameter;

    @DisplayName("process 단위 테스트 ")
    @Test
    void process() throws Exception {
        // given
        when(settleParameter.getSettleType()).thenReturn(SettleType.DAILY);
        when(settleParameter.getCriteriaDate()).thenReturn(LocalDate.now());

        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.DAILY));
        orderRepository.save(OrderFixture.createWithoutId(owner1, 1, LocalDateTime.now()));
        orderRepository.save(OrderFixture.createWithoutId(owner1, 1, LocalDateTime.now()));
        rewardRepository.save(RewardFixture.createWithoutId(owner1, BigDecimal.valueOf(10_000), LocalDateTime.now()));

        Owner owner2 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.DAILY));
        orderRepository.save(OrderFixture.createWithoutId(owner2, 1, LocalDateTime.now()));
        rewardRepository.save(RewardFixture.createWithoutId(owner2, BigDecimal.valueOf(10_000), LocalDateTime.now()));
        rewardRepository.save(RewardFixture.createWithoutId(owner2, BigDecimal.valueOf(10_000), LocalDateTime.now()));

        // when
        SettleJobDto owner1Dto = jobProcessor.process(owner1);
        SettleJobDto owner2Dto = jobProcessor.process(owner2);
        // then
        assertAll(
                () -> assertThat(owner1Dto.getOwner().getId()).isEqualTo(owner1.getId()),
                () -> assertThat(owner1Dto.getOrders()).hasSize(2),
                () -> assertThat(owner1Dto.getRewards()).hasSize(1),

                () -> assertThat(owner2Dto.getOwner().getId()).isEqualTo(owner2.getId()),
                () -> assertThat(owner2Dto.getOrders()).hasSize(1),
                () -> assertThat(owner2Dto.getRewards()).hasSize(2)
        );
    }
}