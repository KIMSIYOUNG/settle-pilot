package in.woowa.pilot.admin.security;

import in.woowa.pilot.admin.application.member.MemberService;
import in.woowa.pilot.admin.application.member.dto.request.MemberCreateDto;
import in.woowa.pilot.admin.application.member.dto.response.MemberResponseDto;
import in.woowa.pilot.admin.common.exception.OAuth2AuthenticationProcessingException;
import in.woowa.pilot.admin.security.user.OAuth2UserInfo;
import in.woowa.pilot.admin.security.user.OAuth2UserInfoFactory;
import in.woowa.pilot.admin.security.user.UserPrincipal;
import in.woowa.pilot.core.member.AuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberService memberService;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        AuthProvider provider = getProvider(oAuth2UserRequest);
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.get(
                provider.name(),
                oAuth2User.getAttributes()
        );

        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("로그인한 매체에서 이메일을 설정해주세요.");
        }

        MemberResponseDto webMemberResponseDto = memberService.findOrElseCreate(
                oAuth2UserInfo.getEmail(),
                provider,
                new MemberCreateDto(provider, oAuth2UserInfo)
        );

        return UserPrincipal.create(webMemberResponseDto);
    }

    private AuthProvider getProvider(OAuth2UserRequest oAuth2UserRequest) {
        return AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());
    }
}
