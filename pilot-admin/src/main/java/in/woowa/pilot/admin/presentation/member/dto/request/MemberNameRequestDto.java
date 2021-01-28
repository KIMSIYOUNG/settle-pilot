package in.woowa.pilot.admin.presentation.member.dto.request;

import in.woowa.pilot.admin.application.member.dto.request.MemberNameDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class MemberNameRequestDto {

    @NotBlank(message = "변경하고자 하는 이름은 필수사항입니다.")
    private String name;

    public MemberNameDto toServiceDto() {
        return new MemberNameDto(name);
    }
}
