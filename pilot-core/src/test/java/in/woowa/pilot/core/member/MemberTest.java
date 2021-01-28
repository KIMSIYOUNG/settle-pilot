package in.woowa.pilot.core.member;

import in.woowa.pilot.core.common.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {
    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.testBuilder()
                .name("김시영")
                .email(UUID.randomUUID().toString() + "@woowahan.com")
                .provider(AuthProvider.GOOGLE)
                .build();
    }

    @DisplayName("우아한형제들 직원(@woowahan)만 회원 생성이 가능하다.")
    @Test
    void create() {
        // when, then
        assertThatThrownBy(() -> Member.builder().email("siyoung@naver.com").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("우아한형제들 직원만 가입할 수 있습니다.");
    }

    @DisplayName("기본적으로 회원은 NORMAL 유저로 생성된다.")
    @Test
    void createWithNormal() {
        // given
        Member member = Member.builder()
                .name("김시영")
                .email("siyoung@woowahan.com")
                .provider(AuthProvider.LOCAL)
                .build();
        // when, then
        assertThat(member.isSameRole(Role.NORMAL)).isTrue();
        assertThat(member.getRole().getTitle()).isEqualTo("우아한형제들 직원");
    }

    @DisplayName("회원의 이름이 정상적으로 변경된다.")
    @Test
    void changeName() {
        // when
        String changedName = "CHANGED NAME";
        member.changeName(changedName);
        // then
        assertThat(member.getName()).isEqualTo(changedName);
    }

    @DisplayName("이름 변경에 null 혹은 값을 넣지 않으면 UNKNOWN으로 변경된다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "              "})
    void changeDefaultMemberName(String input) {
        // when
        member.changeName(input);
        // then
        assertThat(member.getName()).isEqualTo("UNKNOWN");
    }

    @DisplayName("동일한 역할을 가진 유저를 판별할 수 있다.")
    @Test
    void isSameRole() {
        // given
        assertThat(member.isSameRole(Role.NORMAL)).isTrue();
        // when
        member.changeRole(Role.ADMIN);
        // then
        assertThat(member.isSameRole(Role.ADMIN)).isTrue();
    }

    @DisplayName("다른 역할을 비교하는 경우 False를 반환한다.")
    @Test
    void isNotSameRole() {
        // when, then
        assertThat(member.isSameRole(Role.ADMIN)).isFalse();
    }

    @DisplayName("회원이 다시 가입하는 경우, 기존 회원을 재활성화한다.")
    @Test
    void activate() {
        // given
        member.delete();
        // when
        member.activate();
        // then
        assertThat(member.getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(member.getRole()).isEqualTo(Role.NORMAL);
    }
}