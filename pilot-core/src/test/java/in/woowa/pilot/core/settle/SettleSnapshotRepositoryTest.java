package in.woowa.pilot.core.settle;

import in.woowa.pilot.fixture.RepositoryTest;
import in.woowa.pilot.fixture.owner.OwnerFixture;
import in.woowa.pilot.fixture.settle.SettleFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SettleSnapshotRepositoryTest extends RepositoryTest {

    @Autowired
    SettleSnapshotRepository snapshotRepository;

    @DisplayName("특정 기간의 Snapshot을 조회할 수 있다.")
    @Test
    void findByPeriod() {
        // given
        snapshotRepository.saveAll(Arrays.asList(
                SettleSnapshot.builder()
                        .type(SettleSnapshotType.BATCH)
                        .settles(Arrays.asList(SettleFixture.createWithoutId(OwnerFixture.createWithId())))
                        .startAt(LocalDateTime.of(2020, 12, 1, 0, 0, 0, 0))
                        .endAt(LocalDateTime.now())
                        .build(),

                SettleSnapshot.builder()
                        .type(SettleSnapshotType.BATCH)
                        .settles(Arrays.asList(SettleFixture.createWithoutId(OwnerFixture.createWithId())))
                        .startAt(LocalDateTime.of(2020, 12, 5, 0, 0, 0, 0))
                        .endAt(LocalDateTime.now())
                        .build(),

                SettleSnapshot.builder()
                        .type(SettleSnapshotType.BATCH)
                        .settles(Arrays.asList(SettleFixture.createWithoutId(OwnerFixture.createWithId())))
                        .startAt(LocalDateTime.of(2020, 12, 8, 0, 0, 0, 0))
                        .endAt(LocalDateTime.now())
                        .build()
        ));
        // when
        List<SettleSnapshot> snapshots = snapshotRepository.findByPeriod(
                LocalDateTime.of(2020, 12, 5, 0, 0, 0, 0),
                LocalDateTime.now()
        );
        // then
        assertThat(snapshots).hasSize(2);
    }
}