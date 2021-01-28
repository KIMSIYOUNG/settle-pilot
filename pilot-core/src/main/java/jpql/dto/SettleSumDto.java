package jpql.dto;

import in.woowa.pilot.core.settle.SettleSum;
import in.woowa.pilot.core.settle.SettleType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SettleSumDto {

    private BigDecimal totalAmount;

    public SettleSumDto(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public SettleSum toSettleSum(SettleType type, LocalDate criteriaDate) {
        return SettleSum.builder()
                .totalAmount(totalAmount)
                .transactionStartAt(type.getStartCriteriaAt(criteriaDate))
                .transactionEndAt(type.getEndCriteriaAt(criteriaDate))
                .settleScheduleDate(LocalDateTime.now().plusDays(4))
                .settleType(type)
                .build();
    }
}
