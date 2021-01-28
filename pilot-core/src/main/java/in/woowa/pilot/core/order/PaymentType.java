package in.woowa.pilot.core.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum PaymentType {
    CARD("카드 결제", Collections.emptyList(), BigDecimal.valueOf(1)),
    MOBILE("모바일 결제", Collections.emptyList(), BigDecimal.valueOf(1)),
    COUPON("쿠폰 사용", Arrays.asList(PaymentOption.OWNER_COUPON, PaymentOption.BAEMIN_COUPON), BigDecimal.valueOf(1)),
    POINT("포인트 사용", Collections.emptyList(), BigDecimal.valueOf(1)),
    EMPTY("없음", Collections.emptyList(), BigDecimal.valueOf(1));

    private final String name;
    private final List<PaymentOption> paymentOptions;
    private final BigDecimal rate;

    public static PaymentType of(String paymentOption) {
        return Arrays.stream(PaymentType.values())
                .filter(pt -> pt.name().equalsIgnoreCase(paymentOption))
                .findFirst()
                .orElse(PaymentType.EMPTY);
    }

    public BigDecimal calculate(PaymentOption paymentOption) {
        if (paymentOptions.isEmpty()) {
            return this.rate;
        }

        return paymentOptions.stream()
                .filter(option -> option == paymentOption)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 결제수단입니다."))
                .getRate();
    }
}
