package in.woowa.pilot.admin.application.member.dto.response;

import in.woowa.pilot.core.member.AuthProvider;
import in.woowa.pilot.core.member.Member;
import in.woowa.pilot.core.member.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String name;
    private String email;
    private AuthProvider provider;
    private Role role;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.provider = member.getProvider();
        this.role = member.getRole();
    }
}
