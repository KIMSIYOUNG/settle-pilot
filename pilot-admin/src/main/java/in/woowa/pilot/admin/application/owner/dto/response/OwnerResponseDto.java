package in.woowa.pilot.admin.application.owner.dto.response;

import in.woowa.pilot.admin.application.account.AccountResponseDto;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.settle.SettleType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OwnerResponseDto {
    private Long id;
    private String name;
    private String email;
    private SettleType settleType;
    private AccountResponseDto account;

    public OwnerResponseDto(Owner owner) {
        this.id = owner.getId();
        this.name = owner.getName();
        this.email = owner.getEmail();
        this.settleType = owner.getSettleType();
        this.account = new AccountResponseDto(owner.getAccount());
    }
}
