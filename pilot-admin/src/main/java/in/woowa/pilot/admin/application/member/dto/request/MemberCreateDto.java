package in.woowa.pilot.admin.application.member.dto.request;

import in.woowa.pilot.admin.security.user.OAuth2UserInfo;
import in.woowa.pilot.core.member.AuthProvider;
import in.woowa.pilot.core.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberCreateDto {
    private final String name;
    private final String email;
    private final AuthProvider authProvider;

    @Builder
    public MemberCreateDto(String name, String email, AuthProvider authProvider) {
        this.name = name;
        this.email = email;
        this.authProvider = authProvider;
    }

    public MemberCreateDto(AuthProvider provider, OAuth2UserInfo oAuth2UserInfo) {
        this.name = oAuth2UserInfo.getName();
        this.email = oAuth2UserInfo.getEmail();
        this.authProvider = provider;
    }

    public Member toMember() {
        return Member.builder()
                .name(name)
                .email(email)
                .provider(authProvider)
                .build();
    }
}
