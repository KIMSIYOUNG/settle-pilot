package in.woowa.pilot.admin.presentation.owner.dto.request;

import in.woowa.pilot.admin.application.owner.dto.request.OwnerSearchDto;
import in.woowa.pilot.core.settle.SettleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OwnerSearchRequestDto {
    private Long ownerId;
    private String name;
    private String email;
    private SettleType settleType;

    public OwnerSearchDto toAppCondition() {
        return OwnerSearchDto.builder()
                .ownerId(ownerId)
                .name(name)
                .email(email)
                .settleType(settleType)
                .build();
    }
}
