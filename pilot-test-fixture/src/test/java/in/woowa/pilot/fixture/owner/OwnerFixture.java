package in.woowa.pilot.fixture.owner;

import in.woowa.pilot.core.account.Account;
import in.woowa.pilot.core.account.AccountNumber;
import in.woowa.pilot.core.account.AccountType;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.settle.SettleType;

import java.util.UUID;

public class OwnerFixture {
    public static final String TEST_NAME = "OWNER";
    public static final String TEST_EMAIL = "owner@gmail.com";

    public static Owner createWithoutId() {
        return Owner.testBuilder()
                .name(TEST_NAME)
                .email(UUID.randomUUID() + TEST_EMAIL)
                .account(account())
                .settleType(SettleType.DAILY)
                .build();
    }

    public static Owner createWithoutId(SettleType settleType) {
        return Owner.testBuilder()
                .name(TEST_NAME)
                .email(UUID.randomUUID() + TEST_EMAIL)
                .account(account())
                .settleType(settleType)
                .build();
    }

    public static Owner createWithId() {
        return Owner.testBuilder()
                .id(1L)
                .name(TEST_NAME)
                .email(TEST_EMAIL)
                .account(account())
                .settleType(SettleType.DAILY)
                .build();
    }

    public static Owner createWithId(Long id) {
        return createWithId(id, TEST_NAME, TEST_EMAIL);
    }

    public static Owner createWithId(long id, String name, String email) {
        return Owner.testBuilder()
                .id(id)
                .name(name)
                .email(email)
                .account(account())
                .settleType(SettleType.DAILY)
                .build();
    }

    public static Owner createWithoutId(String name, String email) {
        return Owner.testBuilder()
                .name(name)
                .email(email)
                .account(account())
                .settleType(SettleType.DAILY)
                .build();
    }

    private static Account account() {
        return Account.builder()
                .accountNumber(new AccountNumber(UUID.randomUUID().toString()))
                .accountType(AccountType.PAYPAL)
                .build();
    }

}
