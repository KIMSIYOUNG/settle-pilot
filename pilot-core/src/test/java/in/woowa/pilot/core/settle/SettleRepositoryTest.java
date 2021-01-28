package in.woowa.pilot.core.settle;

import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.owner.OwnerRepository;
import in.woowa.pilot.fixture.RepositoryTest;
import in.woowa.pilot.fixture.owner.OwnerFixture;
import in.woowa.pilot.fixture.settle.SettleFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SettleRepositoryTest extends RepositoryTest {

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    SettleRepository settleRepository;

    @DisplayName("삭제된 보정금액은 조회되지 않는다.")
    @Test
    void findById() {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());
        Settle settle = settleRepository.save(SettleFixture.createWithoutId(owner));
        // when
        settle.delete();
        // then
        Optional<Settle> findSettle = settleRepository.findById(settle.getId());

        assertThat(findSettle.isPresent()).isFalse();
    }

    @DisplayName("전체조회에도 삭제된 보정금액은 조회되지 않는다.")
    @Test
    void findAll() {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());
        Settle settle1 = settleRepository.save(SettleFixture.createWithoutId(owner));
        Settle settle2 = settleRepository.save(SettleFixture.createWithoutId(owner));
        Settle settle3 = settleRepository.save(SettleFixture.createWithoutId(owner));
        // when
        settle1.delete();
        settle2.delete();
        settle3.delete();
        // then
        List<Settle> settles = settleRepository.findAll();

        assertThat(settles).hasSize(0);
    }

}