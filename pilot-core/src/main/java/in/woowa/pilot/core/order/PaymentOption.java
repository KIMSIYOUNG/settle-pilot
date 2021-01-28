package in.woowa.pilot.core.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum PaymentOption {
    OWNER_COUPON("업주발행쿠폰", BigDecimal.valueOf(-1)),
    BAEMIN_COUPON("배민발행쿠폰", BigDecimal.valueOf(1)),
    EMPTY("없음", BigDecimal.valueOf(1));

    private final String name;
    private final BigDecimal rate;

    public static PaymentOption of(String paymentOption) {
        if (paymentOption == null) {
            return EMPTY;
        }

        return Arrays.stream(PaymentOption.values())
                .filter(po -> po.name().equalsIgnoreCase(paymentOption))
                .findFirst()
                .orElse(EMPTY);
    }
}
