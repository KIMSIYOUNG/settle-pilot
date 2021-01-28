package in.woowa.pilot.admin.presentation.owner;

import in.woowa.pilot.admin.application.owner.OwnerService;
import in.woowa.pilot.admin.common.exception.ResourceNotFoundException;
import in.woowa.pilot.admin.presentation.DocsWithSecuritySetup;
import in.woowa.pilot.admin.presentation.owner.dto.request.OwnerCreateRequestDto;
import in.woowa.pilot.admin.util.Owners;
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

@WebMvcTest(controllers = OwnerController.class, excludeAutoConfiguration = QueryDslConfiguration.class)
public class OwnerControllerTest extends DocsWithSecuritySetup {

    @MockBean
    OwnerService ownerService;

    @DisplayName("관리자는 업주를 생성할 수 있다.")
    @Test
    void create() throws Exception {
        when(ownerService.create(any())).thenReturn(Owners.createResponseDto());

        mockMvc.perform(post("/api/owners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(Owners.webCreateRequestDto()))
        )
                .andExpect(status().isCreated())
                .andDo(OwnerDocumentation.create());
    }

    @DisplayName("잘못된 enum타입이 인자로 들어오는 경우 400 예외를 반환한다.")
    @Test
    void createWrongEnumType() throws Exception {
        mockMvc.perform(post("/api/owners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"name\":\"OWNER\"," +
                                "\"email\":\"7e2666ae-2479-4353-8238-8bc930dedffaowner@gmail.com\"," +
                                "\"settleType\":\"DASDASD\"," +
                                "\"account\":{" + "" +
                                "   \"accountType\":\"BANK\"," +
                                "   \"accountNumber\":{" +
                                "       \"accountNumber\":\"9cbfebb0-f744-4aa0-a1ce-627f056034cc\"" +
                                "     }" +
                                "}" +
                                "}"
                )
        )
                .andExpect(status().isBadRequest());
    }

    @DisplayName("업주를 생성할 때 빈 값을 넣으면 예외를 반환한다.")
    @Test
    void createWithNull() throws Exception {
        when(ownerService.create(any())).thenReturn(Owners.createResponseDto());
        OwnerCreateRequestDto emptyRequest = new OwnerCreateRequestDto();

        mockMvc.perform(post("/api/owners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(emptyRequest))
        )
                .andExpect(status().isBadRequest())
                .andDo(OwnerDocumentation.createFail());
    }

    @DisplayName("업주번호로 업주를 조회할 수 있다.")
    @Test
    void findById() throws Exception {
        when(ownerService.findById(anyLong())).thenReturn(Owners.createResponseDto());

        mockMvc.perform(get("/api/owners/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(OwnerDocumentation.findById());
    }

    @DisplayName("존재하지않는 업주번호로 업주를 조회한다.")
    @Test
    void findByNotExistId() throws Exception {
        when(ownerService.findById(anyLong())).thenThrow(new ResourceNotFoundException("owner", "id", 1));

        mockMvc.perform(get("/api/owners/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound())
                .andDo(OwnerDocumentation.findByNotExistId());
    }

    @DisplayName("조건에 맞는 업주를 조회할 수 있다.")
    @Test
    void fetchByCondition() throws Exception {
        when(ownerService.findByCondition(any(), any())).thenReturn(Owners.createPagedResponse());

        mockMvc.perform(get("/api/owners")
                .param("email", "OWNER@woowahan.com")
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(OwnerDocumentation.findByCondition());
    }

    @DisplayName("전체 업주를 검색할 수 있다.")
    @Test
    void findAll() throws Exception {
        when(ownerService.findAll(any())).thenReturn(Owners.createPagedResponse());

        mockMvc.perform(get("/api/owners/all")
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(OwnerDocumentation.findAll());
    }

    @DisplayName("업주의 정보를 수정할 수 있다.")
    @Test
    void update() throws Exception {
        when(ownerService.update(any())).thenReturn(Owners.createResponseDto());

        mockMvc.perform(put("/api/owners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Owners.webCreateUpdateRequest(1L)))
        )
                .andExpect(status().isCreated())
                .andDo(OwnerDocumentation.update());
    }

    @DisplayName("업주를 삭제할 수 있다.")
    @Test
    void deleteById() throws Exception {
        doNothing().when(ownerService).delete(anyLong());

        mockMvc.perform(delete("/api/owners/{id}", 1)
        )
                .andExpect(status().isNoContent())
                .andDo(OwnerDocumentation.delete());
    }
}
