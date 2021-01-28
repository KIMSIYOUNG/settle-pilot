package in.woowa.pilot.fixture;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BaseFixture {
    public static final LocalDateTime YESTERDAY_MIDNIGHT = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIDNIGHT);
    public static final LocalDateTime TODAY_MIDNIGHT = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
    public static final LocalDateTime NOW = LocalDateTime.now();
    public static final Pageable DEFAULT_PAGEABLE = PageRequest.of(0, 10);
}
