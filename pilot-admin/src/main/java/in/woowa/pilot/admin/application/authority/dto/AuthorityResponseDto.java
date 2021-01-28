package in.woowa.pilot.admin.application.authority.dto;

import in.woowa.pilot.core.authority.Authority;
import in.woowa.pilot.core.common.Status;
import in.woowa.pilot.core.member.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthorityResponseDto {
    private Long id;
    private Role target;

    public AuthorityResponseDto(Authority authority) {
        if (authority == null || authority.getStatus() == Status.DELETED) {
            return;
        }
        this.id = authority.getId();
        this.target = authority.getTarget();
    }
}
