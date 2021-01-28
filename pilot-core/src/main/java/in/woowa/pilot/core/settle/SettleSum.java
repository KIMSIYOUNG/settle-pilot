package in.woowa.pilot.core.settle;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class SettleSum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private SettleType settleType;

    @Column(nullable = false)
    private LocalDateTime transactionStartAt;

    @Column(nullable = false)
    private LocalDateTime transactionEndAt;

    @Column(nullable = false)
    private LocalDateTime settleScheduleDate;

    @Builder
    public SettleSum(
            BigDecimal totalAmount,
            SettleType settleType,
            LocalDateTime transactionStartAt,
            LocalDateTime transactionEndAt,
            LocalDateTime settleScheduleDate
    ) {
        this.settleType = settleType;
        this.transactionStartAt = transactionStartAt;
        this.transactionEndAt = transactionEndAt;
        this.settleScheduleDate = settleScheduleDate;
        this.totalAmount = totalAmount;
    }
}
