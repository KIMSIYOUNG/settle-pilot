package in.woowa.pilot.core.settle;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.function.Function;

@RequiredArgsConstructor
public enum SettleType {
    DAILY(DateTimeUtils::dailyStartDateTime, DateTimeUtils::dailyEndDateTime),
    WEEK(DateTimeUtils::weekStartDateTime, DateTimeUtils::weekEndDateTime),
    MONTH(DateTimeUtils::monthStartDateTime, DateTimeUtils::monthEndDateTime);

    private final Function<LocalDate, LocalDateTime> startAt;
    private final Function<LocalDate, LocalDateTime> endAt;

    public static SettleType of(String type) {
        return Arrays.stream(SettleType.values())
                .filter(st -> st.name().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("일/주/월 단위 정산을 선택해주세요."));
    }

    public LocalDateTime getStartCriteriaAt(LocalDate date) {
        return startAt.apply(date);
    }

    public LocalDateTime getEndCriteriaAt(LocalDate date) {
        return endAt.apply(date);
    }
}
