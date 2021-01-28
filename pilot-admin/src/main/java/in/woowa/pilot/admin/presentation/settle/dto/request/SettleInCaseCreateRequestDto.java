package in.woowa.pilot.admin.presentation.settle.dto.request;

import in.woowa.pilot.admin.application.settle.dto.request.SettleInCaseCreateDto;
import in.woowa.pilot.core.settle.SettleType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class SettleInCaseCreateRequestDto {

    @NotNull(message = "지급금 대상 업주는 필수항목입니다.")
    private Long ownerId;

    @NotNull(message = "지급금 타입은 필수항목입니다.")
    private SettleType settleType;

    @NotNull(message = "지급금 기준일은 필수항목입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate criteriaDate;

    @Builder(builderClassName = "testBuilder", builderMethodName = "testBuilder")
    public SettleInCaseCreateRequestDto(Long ownerId, SettleType settleType, LocalDate criteriaDate) {
        this.ownerId = ownerId;
        this.settleType = settleType;
        this.criteriaDate = criteriaDate;
    }

    public SettleInCaseCreateDto toServiceDto() {
        return SettleInCaseCreateDto.builder()
                .ownerId(ownerId)
                .settleType(settleType)
                .criteriaDate(criteriaDate)
                .build();
    }
}
