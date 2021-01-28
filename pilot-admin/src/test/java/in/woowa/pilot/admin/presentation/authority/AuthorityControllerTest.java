package in.woowa.pilot.admin.presentation.authority;

import in.woowa.pilot.admin.application.authority.AuthorityService;
import in.woowa.pilot.admin.presentation.DocsWithSecuritySetup;
import in.woowa.pilot.core.config.QueryDslConfiguration;
import in.woowa.pilot.core.member.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthorityController.class, excludeAutoConfiguration = QueryDslConfiguration.class)
class AuthorityControllerTest extends DocsWithSecuritySetup {

    @MockBean
    AuthorityService authorityService;

    @DisplayName("권한요청을 생성할 수 있다.")
    @Test
    void create() throws Exception {
        when(authorityService.create(any())).thenReturn(AuthorityFixture.createResponse());

        mockMvc.perform(post("/api/authorities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new AuthorityRequestDto(1L, Role.ADMIN)))
        )
                .andExpect(status().isCreated())
                .andDo(AuthorityDocumentation.create());
    }

    @DisplayName("권한 요청을 승인할 수 있다.")
    @Test
    void approve() throws Exception {
        doNothing().when(authorityService).approve(anyLong());

        mockMvc.perform(patch("/api/authorities/approve/{id}", 1L)

        )
                .andExpect(status().isNoContent())
                .andDo(AuthorityDocumentation.approve());
    }

    @DisplayName("권한 요청을 거절할 수 있다.")
    @Test
    void reject() throws Exception {
        doNothing().when(authorityService).reject(anyLong());

        mockMvc.perform(patch("/api/authorities/approve/{id}", 1L)

        )
                .andExpect(status().isNoContent())
                .andDo(AuthorityDocumentation.reject());
    }
}