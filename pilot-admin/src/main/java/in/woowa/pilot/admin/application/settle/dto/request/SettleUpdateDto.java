package in.woowa.pilot.admin.application.settle.dto.request;

import in.woowa.pilot.core.settle.SettleStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SettleUpdateDto {
    private final Long id;
    private final SettleStatus settleStatus;

    @Builder
    public SettleUpdateDto(Long id, SettleStatus settleStatus) {
        this.id = id;
        this.settleStatus = settleStatus;
    }
}
