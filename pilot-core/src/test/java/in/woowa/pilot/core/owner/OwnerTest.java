package in.woowa.pilot.core.owner;

import in.woowa.pilot.core.account.Account;
import in.woowa.pilot.core.account.AccountNumber;
import in.woowa.pilot.core.account.AccountType;
import in.woowa.pilot.core.settle.SettleType;
import in.woowa.pilot.fixture.owner.OwnerFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OwnerTest {

    @DisplayName("지급금 유형이 없으면 예외를 반환한다.")
    @Test
    void withoutSettleType() {
        assertThatThrownBy(() -> Owner.builder()
                .settleType(null)
                .account(Account.builder()
                        .accountType(AccountType.BANK)
                        .accountNumber(new AccountNumber("AAAA"))
                        .build())
                .name("김시영")
                .email("siyoung@woowahan.com")
                .build()
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지급금 생성 타입은 필수항목입니다.");
    }

    @DisplayName("계좌정보가 없으면 예외를 반환한다.")
    @Test
    void withoutAccountInfo() {
        assertThatThrownBy(() -> Owner.builder()
                .settleType(SettleType.DAILY)
                .account(null)
                .name("김시영")
                .email("siyoung@woowahan.com")
                .build()
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("계좌 정보는 필수항목입니다.");
    }

    @DisplayName("이름이나 Email, SettleType이 Null인 경우 정보를 수정할 수 없다.")
    @ParameterizedTest
    @MethodSource(value = "provideStringAndNull")
    void updateNull(String name, String email, SettleType settleType) {
        // given
        Owner owner = OwnerFixture.createWithoutId();
        // when , then
        assertThatThrownBy(() -> owner.update(name, email, settleType))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정상적으로 업주의 정보를 업데이트 할 수 있다.")
    @Test
    void update() {
        // given
        Owner owner = OwnerFixture.createWithoutId();
        // when
        owner.update("CHANGED_NAME", "CHANGED_EMAIL", SettleType.DAILY);
        // then
        assertThat(owner.getName()).isEqualTo("CHANGED_NAME");
        assertThat(owner.getEmail()).isEqualTo("CHANGED_EMAIL");
    }

    private static Stream<Arguments> provideStringAndNull() {
        return Stream.of(
                Arguments.of(null, "CHANGED_EMAIL", SettleType.DAILY),
                Arguments.of("CHANGED_NAME", null, SettleType.MONTH),
                Arguments.of(null, null, null),
                Arguments.of("CHANGED_NAME", "CHANGED_EMAIL", null)
        );
    }
}