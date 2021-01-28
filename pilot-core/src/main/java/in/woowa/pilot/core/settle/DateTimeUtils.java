package in.woowa.pilot.core.settle;

import lombok.experimental.UtilityClass;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Locale;

@UtilityClass
public class DateTimeUtils {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final int FIRST_DAY = 1;

    public static LocalDateTime dailyStartDateTime(LocalDate start) {
        return LocalDateTime.of(start, LocalTime.MIDNIGHT);
    }

    public static LocalDateTime dailyEndDateTime(LocalDate end) {

        return LocalDateTime.of(end.plusDays(1), LocalTime.MIDNIGHT);
    }

    public static LocalDateTime weekStartDateTime(LocalDate start) {
        DayOfWeek firstDayOfWeek = getFirstDayOfWeek();
        LocalDate firstDate = start.with(TemporalAdjusters.previousOrSame(firstDayOfWeek));

        return LocalDateTime.of(firstDate, LocalTime.MIDNIGHT);
    }

    public static LocalDateTime weekEndDateTime(LocalDate end) {
        DayOfWeek lastDayOfWeek = getLastDayOfWeek();
        LocalDate lastDay = end.with(TemporalAdjusters.nextOrSame(lastDayOfWeek));

        return LocalDateTime.of(lastDay.plusDays(1), LocalTime.MIDNIGHT);
    }

    public static LocalDateTime monthStartDateTime(LocalDate start) {
        return LocalDateTime.of(LocalDate.of(start.getYear(), start.getMonth(), FIRST_DAY), LocalTime.MIDNIGHT);
    }

    public static LocalDateTime monthEndDateTime(LocalDate end) {
        Month currentMonth = end.getMonth();
        Month nextMonth = currentMonth.plus(1);

        return (nextMonth == Month.JANUARY)
                ? LocalDateTime.of(LocalDate.of(end.getYear() + 1, nextMonth, FIRST_DAY), LocalTime.MIDNIGHT)
                : LocalDateTime.of(LocalDate.of(end.getYear(), nextMonth, FIRST_DAY), LocalTime.MIDNIGHT);
    }

    private static DayOfWeek getFirstDayOfWeek() {
        return WeekFields.of(Locale.KOREA).getFirstDayOfWeek();
    }

    private static DayOfWeek getLastDayOfWeek() {
        return DayOfWeek.of(((getFirstDayOfWeek().getValue() + 5) % DayOfWeek.values().length) + 1);
    }

    public static LocalDate toLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
}
