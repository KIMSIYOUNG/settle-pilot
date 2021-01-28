package in.woowa.pilot.fixture.member;

import in.woowa.pilot.core.member.AuthProvider;
import in.woowa.pilot.core.member.Member;
import in.woowa.pilot.core.member.Role;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MemberFixture {
    public static final String TEST_NAME = "TEST_NAME";
    public static final String TEST_EMAIL = "siyoung@woowahan.com";

    public static Member create(AuthProvider provider) {
        return Member.testBuilder()
                .name(TEST_NAME)
                .email(UUID.randomUUID().toString() + "@woowahan.com")
                .provider(provider)
                .build();
    }

    public static Member createNormalWithoutId() {
        return create(AuthProvider.GOOGLE);
    }

    public static Member createWithId() {
        return Member.testBuilder()
                .id(1L)
                .name(TEST_NAME)
                .email(TEST_EMAIL)
                .provider(AuthProvider.GOOGLE)
                .build();
    }

    public static Member createAdminMember() {
        Member member = createNormalWithoutId();
        member.changeRole(Role.ADMIN);

        return member;
    }

    public static List<Member> createWithCount(int count) {
        return IntStream.range(0, count)
                .mapToObj((i) -> create(AuthProvider.LOCAL))
                .collect(Collectors.toList());
    }
}
