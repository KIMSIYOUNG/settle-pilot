package in.woowa.pilot.admin.presentation.security;

import in.woowa.pilot.admin.application.member.dto.response.MemberResponseDto;
import in.woowa.pilot.admin.application.owner.OwnerService;
import in.woowa.pilot.admin.presentation.DocsWithSecuritySetup;
import in.woowa.pilot.admin.presentation.owner.OwnerController;
import in.woowa.pilot.admin.presentation.owner.OwnerDocumentation;
import in.woowa.pilot.admin.security.CustomOAuth2UserService;
import in.woowa.pilot.admin.security.user.UserPrincipal;
import in.woowa.pilot.admin.util.Members;
import in.woowa.pilot.admin.util.Owners;
import in.woowa.pilot.core.config.QueryDslConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = OwnerController.class, excludeAutoConfiguration = QueryDslConfiguration.class)
public class AuthDocsTest extends DocsWithSecuritySetup {

    @MockBean
    OwnerService ownerService;

    @MockBean
    CustomOAuth2UserService customOAuth2UserService;

    @BeforeEach
    void setUp() {
        MemberResponseDto memberResponse = Members.createMemberResponse();
        when(customOAuth2UserService.loadUser(any())).thenReturn(UserPrincipal.create(memberResponse));
        when(memberService.findOrElseCreate(anyString(), any(), any())).thenReturn(Members.createMemberResponse());
        when(memberService.findById(anyLong())).thenReturn(memberResponse);
    }

    @DisplayName("권한이 없는 회원이 업주를 생성할 때 예외를 반환한다.")
    @Test
    void forbidden() throws Exception {
        MemberResponseDto memberResponse = Members.createMemberResponse();
        UserPrincipal normal = UserPrincipal.create(memberResponse);

        Authentication normalAuthentication = new OAuth2AuthenticationToken(
                normal,
                normal.getAuthorities(),
                memberResponse.getProvider().name()
        );
        SecurityContextHolder.getContext().setAuthentication(normalAuthentication);

        mockMvc.perform(post("/api/owners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(Owners.createRequestDto()))
        )
                .andExpect(status().isForbidden())
                .andDo(OwnerDocumentation.createForbidden());
    }
}
