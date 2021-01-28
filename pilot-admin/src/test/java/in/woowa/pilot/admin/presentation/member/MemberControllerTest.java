package in.woowa.pilot.admin.presentation.member;

import in.woowa.pilot.admin.presentation.DocsWithSecuritySetup;
import in.woowa.pilot.admin.util.Members;
import in.woowa.pilot.core.authority.AuthorityRepository;
import in.woowa.pilot.core.config.QueryDslConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MemberController.class, excludeAutoConfiguration = QueryDslConfiguration.class)
class MemberControllerTest extends DocsWithSecuritySetup {

    @MockBean
    AuthorityRepository authorityRepository;

    @DisplayName("자신의 정보를 조회할 수 있다.")
    @Test
    void findByCurrentMember() throws Exception {
        when(memberService.findById(anyLong())).thenReturn(Members.createMemberResponse());

        mockMvc.perform(get("/api/members")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(MemberDocumentation.findByCurrentMember());
    }

    @DisplayName("모든 회원을 조회할 수 있다.")
    @Test
    void fetchAll() throws Exception {
        when(memberService.fetchMembersWithAuthorities(any(), any())).thenReturn(Members.appMemberpagedResponsesDto());

        mockMvc.perform(get("/api/members/all")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(MemberDocumentation.fetchAll());
    }

    @DisplayName("요청이 없어도 회원의 권한을 변화시킬 수 있다.")
    @Test
    void changeMemberRole() throws Exception {
        doNothing().when(memberService).changeMemberRole(any());

        mockMvc.perform(patch("/api/members/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(Members.webChangeRoleDto(1L)))
        )
                .andExpect(status().isNoContent())
                .andDo(MemberDocumentation.changeMemberRole());
    }

    @DisplayName("특정회원을 탈퇴시킬 수 있다.")
    @Test
    void deleteByAdmin() throws Exception {
        doNothing().when(memberService).deleteById(anyLong());
        when(memberService.findById(anyLong())).thenReturn(Members.createAdminWithId());

        mockMvc.perform(delete("/api/members/{id}", 1))
                .andExpect(status().isNoContent())
                .andDo(MemberDocumentation.deleteByAdmin());
    }

    @DisplayName("스스로 회원탈퇴를 할 수 있다.")
    @Test
    void deleteByCurrentMember() throws Exception {
        doNothing().when(memberService).deleteById(anyLong());

        mockMvc.perform(delete("/api/members"))
                .andExpect(status().isNoContent())
                .andDo(MemberDocumentation.deleteByCurrentMember());
    }
}