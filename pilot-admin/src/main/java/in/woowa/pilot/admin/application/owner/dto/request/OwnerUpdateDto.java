package in.woowa.pilot.admin.application.owner.dto.request;

import in.woowa.pilot.core.settle.SettleType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OwnerUpdateDto {
    private final Long id;
    private final String name;
    private final String email;
    private final SettleType settleType;

    @Builder
    public OwnerUpdateDto(Long id, String name, String email, SettleType settleType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.settleType = settleType;
    }
}
