package in.woowa.pilot.admin.application.account;

import in.woowa.pilot.core.account.Account;
import in.woowa.pilot.core.account.AccountType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountResponseDto {
    private AccountType accountType;
    private String accountNumber;

    public AccountResponseDto(Account account) {
        this.accountType = account.getAccountType();
        this.accountNumber = account.getAccountNumber();
    }
}
