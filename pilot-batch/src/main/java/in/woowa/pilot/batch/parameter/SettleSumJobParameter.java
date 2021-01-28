package in.woowa.pilot.batch.parameter;

import in.woowa.pilot.core.settle.DateTimeUtils;
import in.woowa.pilot.core.settle.SettleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class SettleSumJobParameter {

    private SettleType settleType;

    private LocalDate criteriaDate;

    @Value("#{jobParameters[settleType]}")
    public void setSettleType(String settleType) {
        this.settleType = SettleType.of(settleType);
    }

    @Value("#{jobParameters[criteriaDate]}")
    public void setCriteriaDate(String criteriaDate) {
        this.criteriaDate = DateTimeUtils.toLocalDate(criteriaDate);
    }
}
