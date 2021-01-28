package in.woowa.pilot.core.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    ORDER("주문"),
    ORDER_CONFIRM("주문확인"),
    DELIVERY("배달중"),
    DELIVERY_CONFIRM("배달완료"),
    CANCEL("주문취소");

    private final String name;

    public static OrderStatus of(String status) {
        return Arrays.stream(OrderStatus.values())
                .filter(os -> os.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(status + " 존재하지 않는 상태입니다."));
    }

    public boolean isConfirmed() {
        return this == DELIVERY_CONFIRM || this == CANCEL;
    }
}
