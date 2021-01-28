package in.woowa.pilot.core.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {

    @DisplayName("계좌번호가 없는 경우 빈 문자열을 반환한다.")
    @Test
    void accountNo() {
        // given
        Account account = Account.builder()
                .accountNumber(null)
                .accountType(AccountType.PAYPAL)
                .build();
        // when & then
        assertThat(account.getAccountNumber()).isEqualTo("");
    }
}