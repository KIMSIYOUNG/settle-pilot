package in.woowa.pilot.admin.presentation.member.dto.request;

import in.woowa.pilot.admin.application.member.dto.request.MemberChangeRoleDto;
import in.woowa.pilot.core.member.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class MemberRoleRequestDto {

    @NotNull(message = "바꾸고자 하는 회원번호는 필수사항입니다.")
    private Long memberId;

    @NotNull(message = "바꾸고자 하는 역할은 필수사항입니다.")
    private Role role;

    @Builder(builderClassName = "testBuilder", builderMethodName = "testBuilder")
    public MemberRoleRequestDto(Long memberId, Role role) {
        this.memberId = memberId;
        this.role = role;
    }

    public MemberChangeRoleDto toServiceDto() {
        return MemberChangeRoleDto.builder()
                .memberId(memberId)
                .role(role)
                .build();
    }
}
