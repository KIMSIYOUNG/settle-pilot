package in.woowa.pilot.batch.repository;

import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.owner.OwnerRepository;
import in.woowa.pilot.core.reward.Reward;
import in.woowa.pilot.core.reward.RewardRepository;
import in.woowa.pilot.core.settle.Settle;
import in.woowa.pilot.core.settle.SettleRepository;
import in.woowa.pilot.core.settle.SettleType;
import in.woowa.pilot.fixture.IntegrationTest;
import in.woowa.pilot.fixture.owner.OwnerFixture;
import in.woowa.pilot.fixture.reward.RewardFixture;
import in.woowa.pilot.fixture.settle.SettleFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class RewardCustomRepositoryTest extends IntegrationTest {

    @Autowired
    RewardRepository rewardRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    RewardCustomRepository rewardCustomRepository;

    @Autowired
    RewardBatchRepository rewardBatchRepository;

    @Autowired
    SettleRepository settleRepository;

    @DisplayName("지급되지 않은 보정금액을 업주기준으로 조회할 수 있다.")
    @Test
    void findUnSettledOrderByOwner() {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.DAILY));
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.DAILY));

        Reward reward1 = rewardRepository.save(RewardFixture.createWithoutId(owner, BigDecimal.valueOf(10_000), LocalDateTime.now()));
        Reward reward2 = rewardRepository.save(RewardFixture.createWithoutId(owner, BigDecimal.valueOf(10_000), LocalDateTime.now()));
        Reward reward3 = rewardRepository.save(RewardFixture.createWithoutId(owner, BigDecimal.valueOf(10_000), LocalDateTime.now()));
        rewardRepository.save(RewardFixture.createWithoutId(owner, BigDecimal.valueOf(10_000), LocalDateTime.now().minusDays(1)));
        rewardRepository.save(RewardFixture.createWithoutId(owner1, BigDecimal.valueOf(10_000), LocalDateTime.now()));

        Settle settle = settleRepository.save(SettleFixture.createWithoutId(owner));
        rewardBatchRepository.updateBatchSettle(Arrays.asList(reward3), settle);
        // when
        List<Reward> rewards = rewardCustomRepository.findUnSettledRewardByOwner(owner, SettleType.DAILY, LocalDate.now());
        // then
        assertAll(
                () -> assertThat(rewards).hasSize(2),
                () -> assertThat(rewards.get(0).getId()).isEqualTo(reward1.getId()),
                () -> assertThat(rewards.get(0).getSettle()).isNull(),
                () -> assertThat(rewards.get(1).getId()).isEqualTo(reward2.getId()),
                () -> assertThat(rewards.get(1).getSettle()).isNull()
        );
    }
}