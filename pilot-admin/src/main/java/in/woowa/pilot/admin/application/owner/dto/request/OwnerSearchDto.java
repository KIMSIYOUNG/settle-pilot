package in.woowa.pilot.admin.application.owner.dto.request;

import in.woowa.pilot.core.settle.SettleType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OwnerSearchDto {
    private final Long ownerId;
    private final String name;
    private final String email;
    private final SettleType settleType;

    @Builder
    public OwnerSearchDto(Long ownerId, String name, String email, SettleType settleType) {
        this.ownerId = ownerId;
        this.name = name;
        this.email = email;
        this.settleType = settleType;
    }
}
