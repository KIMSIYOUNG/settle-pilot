package in.woowa.pilot.admin.presentation.settle.dto.request;

import in.woowa.pilot.admin.application.settle.dto.request.SettleUpdateDto;
import in.woowa.pilot.core.settle.SettleStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class SettleUpdateRequestDto {

    @NotNull(message = "수정할 지급금 ID는 필수항목입니다.")
    private Long id;

    @NotNull(message = "변경할 지급금 상태는 필수항목입니다.")
    private SettleStatus settleStatus;

    @Builder(builderClassName = "testBuiler", builderMethodName = "testBuilder")
    public SettleUpdateRequestDto(Long id, SettleStatus settleStatus) {
        this.id = id;
        this.settleStatus = settleStatus;
    }

    public SettleUpdateDto toServiceDto() {
        return SettleUpdateDto.builder()
                .id(id)
                .settleStatus(settleStatus)
                .build();
    }
}
