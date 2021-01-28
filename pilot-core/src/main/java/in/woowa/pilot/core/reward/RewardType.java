package in.woowa.pilot.core.reward;

import java.util.Arrays;

public enum RewardType {
    DELIVERY_ACCIDENT,
    SYSTEM_ERROR,
    ABUSING,
    ETC;

    public static RewardType of(String rewardType) {
        return Arrays.stream(RewardType.values())
                .filter(reward -> reward.name().equalsIgnoreCase(rewardType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 타입입니다. " + rewardType));
    }

    public boolean isMinus() {
        return this == ABUSING;
    }
}
