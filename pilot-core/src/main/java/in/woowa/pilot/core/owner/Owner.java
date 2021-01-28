package in.woowa.pilot.core.owner;

import in.woowa.pilot.core.account.Account;
import in.woowa.pilot.core.common.BaseEntity;
import in.woowa.pilot.core.settle.SettleType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "owner", uniqueConstraints = @UniqueConstraint(name = "UNIQUE_OWNER_EMAIL", columnNames = {"email"}))
@EqualsAndHashCode(of = "id", callSuper = false)
public class Owner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private SettleType settleType;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "owner", cascade = CascadeType.ALL)
    private Account account;

    @Builder
    public Owner(String name, String email, SettleType settleType, Account account) {
        Assert.notNull(settleType, "지급금 생성 타입은 필수항목입니다.");
        Assert.notNull(account, "계좌 정보는 필수항목입니다.");

        this.name = name;
        this.email = email;
        this.settleType = settleType;
        this.addAccount(account);
    }

    @Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
    public Owner(Long id, String name, String email, SettleType settleType, Account account) {
        Assert.notNull(settleType, "지급금 생성 타입은 필수항목입니다.");
        Assert.notNull(account, "계좌 정보는 필수항목입니다.");

        this.id = id;
        this.name = name;
        this.email = email;
        this.settleType = settleType;
        this.addAccount(account);
    }

    public void update(String name, String email, SettleType settleType) {
        if (name == null || email == null || settleType == null) {
            throw new IllegalArgumentException();
        }

        this.name = name;
        this.email = email;
        this.settleType = settleType;
    }

    private void addAccount(Account account) {
        this.account = account;
        account.setOwner(this);
    }
}
