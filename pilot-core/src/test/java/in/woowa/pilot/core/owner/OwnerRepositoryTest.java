package in.woowa.pilot.core.owner;

import in.woowa.pilot.fixture.RepositoryTest;
import in.woowa.pilot.fixture.owner.OwnerFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OwnerRepositoryTest extends RepositoryTest {

    @Autowired
    OwnerRepository ownerRepository;

    @DisplayName("삭제된 업주는 조회되지 않는다.")
    @Test
    void findById() {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());
        // when
        owner.delete();
        Optional<Owner> findOwner = ownerRepository.findById(owner.getId());
        // then
        assertThat(findOwner.isPresent()).isFalse();
    }

    @DisplayName("전체조회에도 삭제된 업주는 조회되지 않는다.")
    @Test
    void findAll() {
        // given
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId());
        Owner owner2 = ownerRepository.save(OwnerFixture.createWithoutId());
        Owner owner3 = ownerRepository.save(OwnerFixture.createWithoutId());
        // when
        owner1.delete();
        // then
        List<Owner> owners = ownerRepository.findAll();

        assertAll(
                () -> assertThat(owners).hasSize(2),
                () -> assertThat(owners.get(0).getId()).isEqualTo(owner2.getId()),
                () -> assertThat(owners.get(1).getId()).isEqualTo(owner3.getId())
        );

    }
}