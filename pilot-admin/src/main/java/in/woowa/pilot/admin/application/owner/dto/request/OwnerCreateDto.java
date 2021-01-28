package in.woowa.pilot.admin.application.owner.dto.request;

import in.woowa.pilot.admin.presentation.owner.dto.request.AccountDto;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.settle.SettleType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OwnerCreateDto {
    private final String name;
    private final String email;
    private final SettleType settleType;
    private final AccountDto accountDto;

    @Builder
    public OwnerCreateDto(String name, String email, SettleType settleType, AccountDto accountDto) {
        this.name = name;
        this.email = email;
        this.settleType = settleType;
        this.accountDto = accountDto;
    }

    public Owner toOwner() {
        return Owner.builder()
                .name(name)
                .email(email)
                .settleType(settleType)
                .account(accountDto.toAccount())
                .build();
    }
}
