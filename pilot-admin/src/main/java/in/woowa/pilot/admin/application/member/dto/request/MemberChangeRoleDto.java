package in.woowa.pilot.admin.application.member.dto.request;

import in.woowa.pilot.core.member.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberChangeRoleDto {
    private final Long memberId;
    private final Role role;

    @Builder
    public MemberChangeRoleDto(Long memberId, Role role) {
        this.memberId = memberId;
        this.role = role;
    }
}
