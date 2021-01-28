package in.woowa.pilot.core.authority;

import in.woowa.pilot.core.common.BaseEntity;
import in.woowa.pilot.core.member.Member;
import in.woowa.pilot.core.member.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Authority extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_MEMBER_AUTHORITY"))
    private Member member;

    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private Role target;

    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private AuthorityStatus authorityStatus;

    @Builder
    public Authority(Member member, Role target, AuthorityStatus authorityStatus) {
        this.member = member;
        this.target = target;
        this.authorityStatus = authorityStatus;
    }

    @Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
    public Authority(Long id, Member member, Role target, AuthorityStatus authorityStatus) {
        this.id = id;
        this.member = member;
        this.target = target;
        this.authorityStatus = authorityStatus;
    }

    public void changeStatus(AuthorityStatus authorityStatus) {
        this.authorityStatus = authorityStatus;
    }
}
