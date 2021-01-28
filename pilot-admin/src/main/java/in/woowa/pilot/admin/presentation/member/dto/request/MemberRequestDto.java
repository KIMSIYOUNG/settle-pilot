package in.woowa.pilot.admin.presentation.member.dto.request;

import in.woowa.pilot.admin.application.member.dto.request.MemberCreateDto;
import in.woowa.pilot.core.member.AuthProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
public class MemberRequestDto {

    @NotBlank(message = "이름은 필수사항입니다.")
    private final String name;

    @NotBlank(message = "이메일은 필수사항입니다.")
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    private final String email;

    @NotBlank(message = "필수사항입니다.")
    private final AuthProvider authProvider;

    public MemberCreateDto toServiceDto() {
        return MemberCreateDto.builder()
                .name(name)
                .email(email)
                .authProvider(authProvider)
                .build();
    }
}
