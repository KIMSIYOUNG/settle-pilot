package in.woowa.pilot.admin.presentation.settle.dto.request;

import in.woowa.pilot.admin.application.settle.dto.request.SettleRegularCreateDto;
import in.woowa.pilot.core.settle.SettleType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class SettleRegularCreateRequestDto {

    @NotNull(message = "지급금 타입은 필수항목입니다.")
    private SettleType settleType;

    @NotNull(message = "지급금 기준일은 필수항목입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate criteriaDate;

    @Builder(builderClassName = "testBuilder", builderMethodName = "testBuilder")
    public SettleRegularCreateRequestDto(SettleType settleType, LocalDate criteriaDate) {
        this.settleType = settleType;
        this.criteriaDate = criteriaDate;
    }

    public SettleRegularCreateDto toServiceDto() {
        return SettleRegularCreateDto.builder()
                .settleType(settleType)
                .criteriaDate(criteriaDate)
                .build();
    }
}
