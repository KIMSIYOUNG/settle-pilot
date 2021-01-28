package in.woowa.pilot.admin.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.woowa.pilot.admin.application.member.MemberService;
import in.woowa.pilot.admin.application.member.dto.response.MemberResponseDto;
import in.woowa.pilot.admin.security.user.UserPrincipal;
import in.woowa.pilot.admin.util.Members;
import in.woowa.pilot.admin.util.SecurityDocumentationConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Import(SecurityDocumentationConfig.class)
@ExtendWith(RestDocumentationExtension.class)
public class DocsWithSecuritySetup {

    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected MemberService memberService;

    @BeforeEach
    public void setUp(
            WebApplicationContext webApplicationContext,
            RestDocumentationContextProvider restDocumentationContextProvider
    ) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentationContextProvider).uris()
                        .withScheme("http")
                        .withHost("localhost")
                        .withPort(5000))
                .apply(springSecurity())
                .alwaysDo(print())
                .build();

        MemberResponseDto adminWithId = Members.createAdminWithId();
        UserPrincipal admin = UserPrincipal.create(adminWithId);


        Authentication adminAuthentication = new OAuth2AuthenticationToken(
                admin,
                admin.getAuthorities(),
                adminWithId.getProvider().name()
        );

        SecurityContextHolder.getContext().setAuthentication(adminAuthentication);
    }
}
