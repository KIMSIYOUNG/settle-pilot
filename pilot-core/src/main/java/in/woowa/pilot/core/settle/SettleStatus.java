package in.woowa.pilot.core.settle;

import java.util.Arrays;

public enum SettleStatus {
    CREATED,
    COMPLETED;

    public static SettleStatus of(String settleStatus) {
        return Arrays.stream(SettleStatus.values())
                .filter(s -> s.name().equalsIgnoreCase(settleStatus))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지급급 상태입니다."));
    }

    public boolean isCompleted() {
        return this == COMPLETED;
    }
}
