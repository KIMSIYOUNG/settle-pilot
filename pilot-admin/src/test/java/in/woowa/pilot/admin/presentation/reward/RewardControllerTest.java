package in.woowa.pilot.admin.presentation.reward;

import in.woowa.pilot.admin.application.reward.RewardService;
import in.woowa.pilot.admin.presentation.DocsWithSecuritySetup;
import in.woowa.pilot.admin.util.Rewards;
import in.woowa.pilot.core.config.QueryDslConfiguration;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.fixture.owner.OwnerFixture;
import org.junit.jupiter.api.BeforeEach;
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

@WebMvcTest(controllers = RewardController.class, excludeAutoConfiguration = QueryDslConfiguration.class)
class RewardControllerTest extends DocsWithSecuritySetup {
    private Owner owner;

    @MockBean
    RewardService rewardService;

    @BeforeEach
    void setUp() {
        owner = OwnerFixture.createWithId();
    }

    @DisplayName("보상금액을 생성할 수 있다.")
    @Test
    void create() throws Exception {
        when(rewardService.create(any())).thenReturn(Rewards.createResponseDto(owner));

        mockMvc.perform(post("/api/rewards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(Rewards.webCreateRequestDto(owner.getId())))
        )
                .andExpect(status().isCreated())
                .andDo(RewardDocumentation.create());
    }

    @DisplayName("기간에 속한 주문에 대해서 보상금액을 제공할 수 있다.")
    @Test
    void createByPeriod() throws Exception {
        doNothing().when(rewardService).createByPeriod(any());

        mockMvc.perform(post("/api/rewards/period")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(Rewards.webRewardPeriodCreateRequest()))
        )
                .andExpect(status().isCreated())
                .andDo(RewardDocumentation.createByPeriod());
    }

    @DisplayName("주문정보를 기반으로 보상금액을 생성할 수 있다.")
    @Test
    void createByOrders() throws Exception {
        doNothing().when(rewardService).createByOrders(any());

        mockMvc.perform(post("/api/rewards/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(Rewards.webRewardOrdersCreateRequest()))
        )
                .andExpect(status().isCreated())
                .andDo(RewardDocumentation.createByOrders());
    }

    @DisplayName("보상금액을 ID로 조회할 수 있다.")
    @Test
    void find() throws Exception {
        when(rewardService.fetchById(anyLong())).thenReturn(Rewards.createResponseDto(owner));

        mockMvc.perform(get("/api/rewards/{id}", 2)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(RewardDocumentation.fetchById());
    }

    @DisplayName("보상금액 전체를 조회할 수 있다.")
    @Test
    void findAll() throws Exception {
        when(rewardService.findAll(any())).thenReturn(Rewards.createPagedResponseDto(owner));

        mockMvc.perform(get("/api/rewards/all")
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(RewardDocumentation.findAll());
    }

    @DisplayName("조건에 따라 보상금액을 조회할 수 있다.")
    @Test
    void fetchByCondition() throws Exception {
        when(rewardService.fetchPagedByCondition(any(), any())).thenReturn(Rewards.createPagedResponseDto(owner));

        mockMvc.perform(get("/api/rewards")
                .param("ownerName", OwnerFixture.TEST_NAME)
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(RewardDocumentation.fetchByCondition());
    }

    @DisplayName("지급금 ID로 보상금액을 조회할 수 있다.")
    @Test
    void findBySettleId() throws Exception {
        when(rewardService.fetchBySettleId(anyLong(), any())).thenReturn(Rewards.createPagedResponseDto(owner));

        mockMvc.perform(get("/api/rewards/settles/{settleId}", 1)
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(RewardDocumentation.fetchBySettleId());
    }

    @DisplayName("보상금액과 관련된 정보를 수정할 수 있다.")
    @Test
    void update() throws Exception {
        doNothing().when(rewardService).update(any());

        mockMvc.perform(put("/api/rewards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(Rewards.webUpdateRequestDto(2L)))
        )
                .andExpect(status().isCreated())
                .andDo(RewardDocumentation.update());
    }

    @DisplayName("보상금액을 아이디로 삭제할 수 있다.")
    @Test
    void deleteById() throws Exception {
        doNothing().when(rewardService).delete(anyLong());

        mockMvc.perform(delete("/api/rewards/{id}", 2)
        )
                .andExpect(status().isNoContent())
                .andDo(RewardDocumentation.delete());
    }
}