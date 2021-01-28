package in.woowa.pilot.admin.presentation.owner.dto.request;

import in.woowa.pilot.core.account.Account;
import in.woowa.pilot.core.account.AccountNumber;
import in.woowa.pilot.core.account.AccountType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class AccountDto {

    @NotNull(message = "계좌타입은 필수항목입니다.")
    private AccountType accountType;

    @NotNull(message = "계좌번호는 필수항목입니다.")
    private AccountNumber accountNumber;

    public AccountDto(AccountType accountType, AccountNumber accountNumber) {
        this.accountType = accountType;
        this.accountNumber = accountNumber;
    }

    public Account toAccount() {
        return Account.builder()
                .accountType(accountType)
                .accountNumber(accountNumber)
                .build();
    }
}
