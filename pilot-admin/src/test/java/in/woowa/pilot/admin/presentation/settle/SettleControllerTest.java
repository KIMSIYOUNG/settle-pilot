package in.woowa.pilot.admin.presentation.settle;

import in.woowa.pilot.admin.application.settle.SettleService;
import in.woowa.pilot.admin.application.settle.dto.response.SettleResponseDto;
import in.woowa.pilot.admin.presentation.DocsWithSecuritySetup;
import in.woowa.pilot.admin.presentation.settle.dto.request.SettleCompleteRequestDto;
import in.woowa.pilot.admin.util.Settles;
import in.woowa.pilot.core.config.QueryDslConfiguration;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.fixture.owner.OwnerFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SettleController.class, excludeAutoConfiguration = QueryDslConfiguration.class)
class SettleControllerTest extends DocsWithSecuritySetup {

    private Owner owner;

    @MockBean
    SettleService settleService;

    @BeforeEach
    void setUp() {
        owner = OwnerFixture.createWithId();
    }

    @DisplayName("예외적인 경우 지급금을 수동으로 생성할 수 있다.")
    @Test
    void createInCaseSettle() throws Exception {
        SettleResponseDto responseDto = Settles.createResponseDto(owner);
        when(settleService.createInCaseSettle(any())).thenReturn(responseDto);

        String content = objectMapper.writeValueAsString(Settles.webCreateRequestDto(owner.getId()));
        mockMvc.perform(post("/api/settles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
                .andExpect(status().isCreated())
                .andDo(SettleDocumentation.create());
    }

    @DisplayName("지급금을 생성할 수 있다.")
    @Test
    void createBatchSettle() throws Exception {
        SettleResponseDto responseDto = Settles.createResponseDto(owner);
        when(settleService.createInCaseSettle(any())).thenReturn(responseDto);

        String content = objectMapper.writeValueAsString(Settles.createRegularRequestDto());
        mockMvc.perform(post("/api/settles/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
                .andExpect(status().isNoContent())
                .andDo(SettleDocumentation.createBatch());
    }

    @DisplayName("검색 조건에 따라서 지급금 결과를 조회할 수 있다.")
    @Test
    void fetchByOwnerAndCondition() throws Exception {
        when(settleService.fetchPagedByCondition(any(), any())).thenReturn(Settles.createPagedResponsesDto(owner));

        mockMvc.perform(get("/api/settles")
                .param("page", "0")
                .param("size", "10")
                .param("ownerId", "1")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(SettleDocumentation.fetchByCondition());
    }

    @DisplayName("지급금의 합계를 조회한다.")
    @Test
    void fetchAmountByCondition() throws Exception {
        when(settleService.fetchAmountByCondition(any())).thenReturn(Settles.createAmountResponseDto(owner));

        mockMvc.perform(get("/api/settles/amount")
                .accept(MediaType.APPLICATION_JSON)
                .param("startAt", LocalDateTime.now().minusDays(3).toString())
                .param("endAt", LocalDateTime.now().toString())
        )
                .andExpect(status().isOk())
                .andDo(SettleDocumentation.fetchAmount());
    }

    @DisplayName("모든 지급금을 조회한다.")
    @Test
    void findAll() throws Exception {
        when(settleService.fetchPagedAll(any())).thenReturn(Settles.createPagedResponsesDto(owner));

        mockMvc.perform(get("/api/settles/all")
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(SettleDocumentation.fetchAll());
    }

    @DisplayName("지급금의 상태를 변경한다.(생성 -> 지급 완료)")
    @Test
    void updateSettleStatus() throws Exception {
        doNothing().when(settleService).updateSettleStatus(any());

        mockMvc.perform(patch("/api/settles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(Settles.webUpdateRequestDto(1L)))
        )
                .andExpect(status().isNoContent())
                .andDo(SettleDocumentation.update());
    }

    @DisplayName("지급금을 일괄적으로 완료처리 할 수 있다.")
    @Test
    void updateBulkStatus() throws Exception {
        doNothing().when(settleService).bulkCompleteSettleStatus(any());
        SettleCompleteRequestDto condition = Settles.webSettleCompleteCondition();

        mockMvc.perform(patch("/api/settles/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(condition))
        )
                .andExpect(status().isNoContent())
                .andDo(SettleDocumentation.updateBulk());
    }

    @DisplayName("지급금을 삭제한다.")
    @Test
    void deleteById() throws Exception {
        doNothing().when(settleService).deleteById(anyLong());

        mockMvc.perform(delete("/api/settles/{id}", 1))
                .andExpect(status().isNoContent())
                .andDo(SettleDocumentation.delete());
    }
}