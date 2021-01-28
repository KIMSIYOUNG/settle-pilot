package in.woowa.pilot.batch.parameter;

import in.woowa.pilot.core.settle.DateTimeUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class OrderPaymentAggregationJobParameter {
    private LocalDate criteriaDate;

    @Value("#{jobParameters[criteriaDate]}")
    public void setCriteriaDate(String criteriaDate) {
        this.criteriaDate = DateTimeUtils.toLocalDate(criteriaDate);
    }
}
