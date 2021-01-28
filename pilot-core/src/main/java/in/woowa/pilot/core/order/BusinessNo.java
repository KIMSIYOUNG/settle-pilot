package in.woowa.pilot.core.order;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class BusinessNo {
    private String businessNo;

    public BusinessNo(LocalDate orderedAt, String random) {
        Objects.requireNonNull(orderedAt, "BusinessNo : 주문일시는 필수사항입니다.");
        Objects.requireNonNull(random, "BusinessNo : 난수는 필수사항입니다.");

        this.businessNo = orderedAt.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + random;
    }

    public void cancel() {
        if (businessNo.contains("CANCEL")) {
            throw new IllegalArgumentException("이미 취소된 주문입니다.");
        }

        this.businessNo = "CANCEL-" + businessNo;
    }
}
