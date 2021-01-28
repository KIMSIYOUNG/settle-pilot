package in.woowa.pilot.core.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Role {
    NORMAL("ROLE_NORMAL", "우아한형제들 직원"),
    ADMIN("ROLE_ADMIN", "정산시스템팀");

    private final String key;
    private final String title;

    public static Role of(String role) {
        return Arrays.stream(Role.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(role + " 존재하지 않는 권한입니다."));
    }
}
