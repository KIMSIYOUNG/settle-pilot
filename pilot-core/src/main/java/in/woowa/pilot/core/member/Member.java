package in.woowa.pilot.core.member;

import in.woowa.pilot.core.common.BaseEntity;
import in.woowa.pilot.core.common.Status;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "member", uniqueConstraints = @UniqueConstraint(name = "UNIQUE_EMAIL", columnNames = {"email"}))
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String name, String email, AuthProvider provider) {
        if (isNotWoowahan(email)) {
            throw new IllegalArgumentException("우아한형제들 직원만 가입할 수 있습니다.");
        }
        this.name = name;
        this.email = email;
        this.role = Role.NORMAL;
        this.provider = provider;
    }

    @Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
    public Member(Long id, String name, String email, AuthProvider provider) {
        if (isNotWoowahan(email)) {
            throw new IllegalArgumentException("우아한형제들 직원만 가입할 수 있습니다.");
        }
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = Role.NORMAL;
        this.provider = provider;
    }

    public void changeName(String name) {
        this.name = Objects.isNull(name) || name.trim().isEmpty() ?
                "UNKNOWN"
                : name;
    }

    public boolean isSameRole(Role role) {
        return Objects.equals(this.role, role);
    }

    public void activate() {
        super.changeStatus(Status.ACTIVE);
        this.role = Role.NORMAL;
    }

    public void changeRole(Role role) {
        this.role = role;
    }

    private boolean isNotWoowahan(String email) {
        int beginIndex = email.indexOf('@') + 1;
        int endIndex = email.lastIndexOf('.');

        String domain = email.substring(beginIndex, endIndex);
        return !domain.equalsIgnoreCase("woowahan");
    }
}
