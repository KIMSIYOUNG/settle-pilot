package in.woowa.pilot.admin.presentation.owner.dto.request;

import in.woowa.pilot.admin.application.owner.dto.request.OwnerUpdateDto;
import in.woowa.pilot.core.settle.SettleType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class OwnerUpdateRequestDto {

    @NotNull(message = "수정할 업주의 ID는 필수사항입니다.")
    private Long id;

    @NotBlank(message = "이름은 필수항목입니다.")
    private String name;

    @Email(message = "이메일 양식을 지켜주세요.")
    @NotBlank(message = "이메일은 필수항목입니다.")
    private String email;

    @NotNull(message = "수정할 업주의 지급금 유형은 필수항목입니다.")
    private SettleType settleType;

    @Builder(builderClassName = "testBuilder", builderMethodName = "testBuilder")
    public OwnerUpdateRequestDto(Long id, String name, String email, SettleType settleType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.settleType = settleType;
    }

    public OwnerUpdateDto toServiceDto() {
        return OwnerUpdateDto.builder()
                .id(id)
                .name(name)
                .email(email)
                .settleType(settleType)
                .build();
    }
}
