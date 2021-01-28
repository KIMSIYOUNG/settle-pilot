package in.woowa.pilot.batch.repository;

import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.owner.OwnerRepository;
import in.woowa.pilot.core.reward.Reward;
import in.woowa.pilot.core.reward.RewardRepository;
import in.woowa.pilot.core.settle.Settle;
import in.woowa.pilot.core.settle.SettleRepository;
import in.woowa.pilot.fixture.IntegrationTest;
import in.woowa.pilot.fixture.owner.OwnerFixture;
import in.woowa.pilot.fixture.reward.RewardFixture;
import in.woowa.pilot.fixture.settle.SettleFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RewardBatchRepositoryTest extends IntegrationTest {

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    RewardRepository rewardRepository;

    @Autowired
    SettleRepository settleRepository;

    @Autowired
    RewardBatchRepository rewardBatchRepository;

    @DisplayName("특정 지급금 번호로 업데이트 시킬 수 있다.")
    @Test
    void update() {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());
        rewardRepository.save(RewardFixture.createWithoutId(owner, BigDecimal.ZERO, LocalDateTime.now()));
        List<Reward> rewards = rewardRepository.saveAll(Arrays.asList(
                RewardFixture.createWithoutId(owner, BigDecimal.ZERO, LocalDateTime.now()),
                RewardFixture.createWithoutId(owner, BigDecimal.ZERO, LocalDateTime.now()),
                RewardFixture.createWithoutId(owner, BigDecimal.ZERO, LocalDateTime.now()),
                RewardFixture.createWithoutId(owner, BigDecimal.ZERO, LocalDateTime.now())
        ));
        Settle settle = settleRepository.save(SettleFixture.createWithoutId(owner));
        // when
        long count = rewardBatchRepository.updateBatchSettle(rewards, settle);
        // then
        assertThat(count).isEqualTo(4);
    }
}