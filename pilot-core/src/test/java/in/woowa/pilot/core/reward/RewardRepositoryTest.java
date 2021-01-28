package in.woowa.pilot.core.reward;

import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.owner.OwnerRepository;
import in.woowa.pilot.fixture.RepositoryTest;
import in.woowa.pilot.fixture.owner.OwnerFixture;
import in.woowa.pilot.fixture.reward.RewardFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class RewardRepositoryTest extends RepositoryTest {

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    RewardRepository rewardRepository;

    @DisplayName("삭제된 보정금액은 조회되지 않는다.")
    @Test
    void findById() {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());
        Reward reward = rewardRepository.save(RewardFixture.createWithoutId(owner, BigDecimal.ZERO, LocalDateTime.now()));
        // when
        reward.delete();
        // then
        Optional<Reward> findReward = rewardRepository.findById(reward.getId());

        assertThat(findReward.isPresent()).isFalse();
    }

    @DisplayName("전체조회에도 삭제된 보정금액은 조회되지 않는다.")
    @Test
    void findAll() {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());
        Reward reward1 = rewardRepository.save(RewardFixture.createWithoutId(owner, BigDecimal.ZERO, LocalDateTime.now()));
        Reward reward2 = rewardRepository.save(RewardFixture.createWithoutId(owner, BigDecimal.ZERO, LocalDateTime.now()));
        Reward reward3 = rewardRepository.save(RewardFixture.createWithoutId(owner, BigDecimal.ZERO, LocalDateTime.now()));
        // when
        reward3.delete();
        // then
        List<Reward> rewards = rewardRepository.findAll();

        assertAll(
                () -> assertThat(rewards).hasSize(2),
                () -> assertThat(rewards.get(0).getId()).isEqualTo(reward1.getId()),
                () -> assertThat(rewards.get(1).getId()).isEqualTo(reward2.getId())
        );
    }
}