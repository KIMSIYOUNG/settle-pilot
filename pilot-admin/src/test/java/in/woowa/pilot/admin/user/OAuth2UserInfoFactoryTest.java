package in.woowa.pilot.admin.user;

import in.woowa.pilot.admin.security.user.GoogleOAuth2UserInfo;
import in.woowa.pilot.admin.security.user.OAuth2UserInfo;
import in.woowa.pilot.admin.security.user.OAuth2UserInfoFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OAuth2UserInfoFactoryTest {
    private final Map<String, Object> attributes = new HashMap<>();

    @BeforeEach
    void setUp() {
        attributes.put("sub", "aaaa");
        attributes.put("name", "test");
        attributes.put("email", "test@email.com");
    }

    @DisplayName("Registration Id를 통해 적절한 OAuth2UserInfo를 생성할 수 있다.")
    @Test
    void get() {
        // when
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.get("google", attributes);
        // then
        assertAll(
                () -> assertThat(oAuth2UserInfo).isInstanceOf(GoogleOAuth2UserInfo.class),
                () -> assertThat(oAuth2UserInfo.getId()).isEqualTo("aaaa"),
                () -> assertThat(oAuth2UserInfo.getName()).isEqualTo("test"),
                () -> assertThat(oAuth2UserInfo.getEmail()).isEqualTo("test@email.com")
        );
    }

    @DisplayName("존재하지 않는 registrationId를 입력하는 경우 예외가 발생한다.")
    @Test
    void getNotExistRegistrationId() {
        // when & then
        assertThatThrownBy(() -> OAuth2UserInfoFactory.get("NOT_EXIST_ID", attributes))
                .isInstanceOf(IllegalArgumentException.class);
    }
}