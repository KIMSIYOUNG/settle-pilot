package in.woowa.pilot.core.member;

import in.woowa.pilot.core.common.Status;
import in.woowa.pilot.fixture.member.MemberFixture;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;


class BaseEntityTest {

    @DisplayName("status가 정상적으로 변경된다.")
    @Test
    void delete() {
        // given
        Member member = MemberFixture.createNormalWithoutId();

        assertThat(member.getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(member.getDeletedAt()).isNull();
        // when
        member.delete();
        // then
        assertThat(member.getStatus()).isEqualTo(Status.DELETED);
        assertThat(member.getDeletedAt())
                .isCloseTo(LocalDateTime.now(), new TemporalUnitWithinOffset(3, ChronoUnit.SECONDS));
    }
}