package in.woowa.pilot.core.reward;

import in.woowa.pilot.core.common.BaseEntity;
import in.woowa.pilot.core.order.BusinessNo;
import in.woowa.pilot.core.order.Order;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.settle.Settle;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "reward", uniqueConstraints = @UniqueConstraint(name = "UNIQUE_REWARD_BUSINESS_NO", columnNames = {"businessNo"}))
public class Reward extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private BusinessNo businessNo;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private RewardType rewardType;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime rewardDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "FK_ORDER_REWARD"))
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "settle_id", foreignKey = @ForeignKey(name = "FK_SETTLE_REWARD"))
    private Settle settle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", foreignKey = @ForeignKey(name = "FK_OWNER_REWARD"))
    private Owner owner;

    @Builder
    public Reward(
            BusinessNo businessNo,
            BigDecimal amount,
            RewardType rewardType,
            String description,
            Owner owner,
            Order order,
            LocalDateTime rewardDateTime
    ) {
        if (rewardDateTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("보정금액의 생성시기는 현재 이전일 수 없습니다.");
        }

        validateAmount(amount, rewardType);
        this.businessNo = businessNo;
        this.amount = amount;
        this.rewardType = rewardType;
        this.description = description;
        this.owner = owner;
        this.order = order;
        this.rewardDateTime = rewardDateTime;
    }

    @Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
    public Reward(
            Long id,
            BusinessNo businessNo,
            BigDecimal amount,
            RewardType rewardType,
            String description,
            Owner owner,
            Order order,
            LocalDateTime rewardDateTime
    ) {
        if (rewardDateTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("보정금액의 생성시기는 현재 이전일 수 없습니다.");
        }

        validateAmount(amount, rewardType);
        this.id = id;
        this.businessNo = businessNo;
        this.amount = amount;
        this.rewardType = rewardType;
        this.description = description;
        this.owner = owner;
        this.order = order;
        this.rewardDateTime = rewardDateTime;
    }

    public void update(BigDecimal amount, String rewardType, String description) {
        Objects.requireNonNull(amount, "금액/타입/이유를 모두 입력해주세요.");
        Objects.requireNonNull(rewardType, "금액/타입/이유를 모두 입력해주세요.");
        Objects.requireNonNull(description, "금액/타입/이유를 모두 입력해주세요.");
        validateAmount(amount, RewardType.valueOf(rewardType));

        this.amount = amount;
        this.rewardType = RewardType.valueOf(rewardType.toUpperCase());
        this.description = description;
    }

    @Override
    public void delete() {
        if (settle != null) {
            throw new IllegalArgumentException("이미 지급금으로 생성된 보상금액은 삭제할 수 없습니다.");
        }
        super.delete();
    }

    public String getBusinessNoValue() {
        if (businessNo == null) {
            return "";
        }

        return businessNo.getBusinessNo();
    }

    private void validateAmount(BigDecimal amount, RewardType rewardType) {
        if (rewardType.isMinus() && amount.compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalArgumentException("어뷰징 금액은 0 혹은 음수만 가능합니다.");
        }
    }
}
