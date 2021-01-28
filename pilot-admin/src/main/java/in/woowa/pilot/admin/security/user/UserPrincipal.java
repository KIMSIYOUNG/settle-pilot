package in.woowa.pilot.admin.security.user;

import in.woowa.pilot.admin.application.member.dto.response.MemberResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Getter
@RequiredArgsConstructor
public class UserPrincipal implements OAuth2User {
    private final Long id;
    private final String name;
    private final Collection<? extends GrantedAuthority> authorities;

    public static UserPrincipal create(MemberResponseDto member) {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(member.getRole().getKey()));

        return new UserPrincipal(
                member.getId(),
                member.getName(),
                authorities
        );
    }

    @Override
    public Map<String, Object> getAttributes() {
        return new HashMap<>();
    }

    @Override
    public String getName() {
        return this.name;
    }
}
