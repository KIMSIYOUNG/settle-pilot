package in.woowa.pilot.admin.security.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum OAuth2UserInfoFactory {
    GOOGLE(GoogleOAuth2UserInfo::new);

    private final Function<Map<String, Object>, OAuth2UserInfo> expression;

    public static OAuth2UserInfo get(String registrationId, Map<String, Object> attributes) {
        return OAuth2UserInfoFactory.valueOf(registrationId.toUpperCase())
                .getExpression()
                .apply(attributes);
    }
}
