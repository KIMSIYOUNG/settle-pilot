package in.woowa.pilot.admin.acceptance.auth;

import in.woowa.pilot.admin.acceptance.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberAuthIntegrationTest extends AcceptanceTest {
    private Runnable session;

    @BeforeEach
    void setUp() {
        session = 일반회원세션();
        session.run();
    }

    @DisplayName("자신의 정보를 조회할 수 있다.")
    @Test
    void findByCurrentMember() throws Exception {
        mockMvc.perform(get("/api/members")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());
    }

    @DisplayName("회원은 다른 회원을 삭제할 수 없다.")
    @Test
    void deleteWithoutAuth() throws Exception {
        mockMvc.perform(delete("/api/members/2")

        )
                .andExpect(status().isForbidden());
    }

    @DisplayName("회원은 회원탈퇴를 할 수 있다.")
    @Test
    void deleteById() throws Exception {
        mockMvc.perform(delete("/api/members")

        )
                .andExpect(status().isNoContent());
    }
}
