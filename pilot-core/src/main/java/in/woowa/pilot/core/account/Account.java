package in.woowa.pilot.core.account;

import in.woowa.pilot.core.owner.Owner;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", foreignKey = @ForeignKey(name = "FK_OWNER_ACCOUNT"))
    private Owner owner;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Embedded
    private AccountNumber accountNumber;

    @Builder
    public Account(AccountType accountType, AccountNumber accountNumber) {
        this.accountType = accountType;
        this.accountNumber = accountNumber;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getAccountNumber() {
        if (accountNumber == null) {
            return "";
        }

        return accountNumber.getAccountNumber();
    }
}
