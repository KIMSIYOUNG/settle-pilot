package in.woowa.pilot.core.settle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

class DateTimeUtilsTest {

    @DisplayName("일단위의 날짜 결과를 정상적으로 변환한다.")
    @Test
    void getByDaily() {
        // when , then
        LocalDateTime start = DateTimeUtils.dailyStartDateTime(LocalDate.now().minusDays(1));
        LocalDateTime end = DateTimeUtils.dailyEndDateTime(LocalDate.now().minusDays(1));

        assertThat(start).isEqualTo(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN));
        assertThat(end).isEqualTo(LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
    }

    @DisplayName("주단위의 날짜 결과를 정상적으로 변환한다.")
    @Test
    void getByWeek() {
        // given
        LocalDate date = LocalDate.of(2020, Month.MARCH, 30);
        // when , then
        LocalDateTime start = DateTimeUtils.weekStartDateTime(date);
        LocalDateTime end = DateTimeUtils.weekEndDateTime(date);

        assertThat(start).isEqualTo(LocalDateTime.of(LocalDate.of(2020, Month.MARCH, 29), LocalTime.MIDNIGHT));
        assertThat(end).isEqualTo(LocalDateTime.of(LocalDate.of(2020, Month.APRIL, 5), LocalTime.MIDNIGHT));
    }

    @DisplayName("월단위의 날짜 결과를 정상적으로 변환한다.")
    @Test
    void getByMonth() {
        // given
        LocalDate date = LocalDate.of(2020, Month.MARCH, 30);
        // when , then
        LocalDateTime start = DateTimeUtils.monthStartDateTime(date);
        LocalDateTime end = DateTimeUtils.monthEndDateTime(date);

        assertThat(start).isEqualTo(LocalDateTime.of(LocalDate.of(2020, Month.MARCH, 1), LocalTime.MIDNIGHT));
        assertThat(end).isEqualTo(LocalDateTime.of(LocalDate.of(2020, Month.APRIL, 1), LocalTime.MIDNIGHT));
    }

    @DisplayName("12월을 조회하는 경우 내년을 반환한다.")
    @Test
    void getByMonthOver() {
        // given
        LocalDate date = LocalDate.of(2020, Month.DECEMBER, 30);
        // when , then
        LocalDateTime start = DateTimeUtils.monthStartDateTime(date);
        LocalDateTime end = DateTimeUtils.monthEndDateTime(date);

        assertThat(start).isEqualTo(LocalDateTime.of(LocalDate.of(2020, Month.DECEMBER, 1), LocalTime.MIDNIGHT));
        assertThat(end).isEqualTo(LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.MIDNIGHT));
    }
}