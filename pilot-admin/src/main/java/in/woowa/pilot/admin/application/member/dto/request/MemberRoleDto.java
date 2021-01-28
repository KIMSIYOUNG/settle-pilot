package in.woowa.pilot.admin.application.member.dto.request;

import in.woowa.pilot.core.member.Role;
import lombok.Getter;

@Getter
public class MemberRoleDto {
    private final Role role;

    public MemberRoleDto(Role role) {
        this.role = role;
    }
}
