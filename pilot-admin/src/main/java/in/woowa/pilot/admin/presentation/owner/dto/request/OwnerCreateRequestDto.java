package in.woowa.pilot.admin.presentation.owner.dto.request;

import in.woowa.pilot.admin.application.owner.dto.request.OwnerCreateDto;
import in.woowa.pilot.core.settle.SettleType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class OwnerCreateRequestDto {

    @NotBlank(message = "이름은 필수항목입니다.")
    private String name;

    @Email(message = "이메일 양식을 지켜주세요.")
    @NotBlank(message = "이메일은 필수항목입니다.")
    private String email;

    @NotNull(message = "지급금 유형은 필수항목입니다.")
    private SettleType settleType;

    @NotNull(message = "계좌정보는 필수항목입니다.")
    private AccountDto account;

    public OwnerCreateRequestDto(String name, String email, SettleType settleType, AccountDto account) {
        this.name = name;
        this.email = email;
        this.settleType = settleType;
        this.account = account;
    }

    public OwnerCreateDto toServiceDto() {
        return OwnerCreateDto.builder()
                .settleType(settleType)
                .accountDto(account)
                .name(name)
                .email(email)
                .build();
    }
}
