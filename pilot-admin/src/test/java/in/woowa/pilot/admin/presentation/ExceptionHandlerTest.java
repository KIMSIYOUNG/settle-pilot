package in.woowa.pilot.admin.presentation;

import in.woowa.pilot.admin.application.order.OrderService;
import in.woowa.pilot.admin.common.ErrorCode;
import in.woowa.pilot.admin.presentation.order.OrderController;
import in.woowa.pilot.admin.presentation.order.dto.request.OrderCreateRequestDto;
import in.woowa.pilot.core.config.QueryDslConfiguration;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = OrderController.class, excludeAutoConfiguration = QueryDslConfiguration.class)
public class ExceptionHandlerTest extends DocsWithSecuritySetup {

    @MockBean
    OrderService orderService;

    @DisplayName("Long타입에 String을 바인딩하는 경우 예외를 반환한다.")
    @Test
    void bindException() throws Exception {
        mockMvc.perform(get("/api/orders")
                .accept(MediaType.APPLICATION_JSON)
                .param("ownerId", "NOT NUMBER, STRING")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(ErrorCode.INVALID_VALIDATE.getStatus()))
                .andExpect(jsonPath("code").value(ErrorCode.INVALID_VALIDATE.getCode()))
                .andExpect(jsonPath("message").value("Invalid Binding please check the search conditions"))
                .andExpect(jsonPath("errors").exists())
                .andDo(ExceptionDocumentation.typeBindException());
    }

    @DisplayName("날짜 포맷을 지키지 않는 경우 예외를 반환한다.")
    @Test
    void bindException2() throws Exception {
        mockMvc.perform(get("/api/orders")
                .accept(MediaType.APPLICATION_JSON)
                .param("startAt", "2020-12-11 22:10:00")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(ErrorCode.INVALID_VALIDATE.getStatus()))
                .andExpect(jsonPath("code").value(ErrorCode.INVALID_VALIDATE.getCode()))
                .andExpect(jsonPath("message").value("Invalid Binding please check the search conditions"))
                .andExpect(jsonPath("errors").exists())
                .andDo(ExceptionDocumentation.datetimeBindException());
    }

    @DisplayName("javax validation 규칙을 지키지 않는 경우 예외를 반한환다.")
    @Test
    void validException() throws Exception {
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new OrderCreateRequestDto()))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(ErrorCode.INVALID_VALIDATE.getStatus()))
                .andExpect(jsonPath("code").value(ErrorCode.INVALID_VALIDATE.getCode()))
                .andExpect(jsonPath("message").value("Argument Invalid"))
                .andExpect(jsonPath("errors").exists())
                .andDo(ExceptionDocumentation.validException());
    }

    @DisplayName("Jackson이 파싱할 수 없는 요청에 대해 예외를 반환한다.")
    @Test
    void httpMessageNotReadable() throws Exception {
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{weird: unexpected}")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(ErrorCode.INVALID_VALIDATE.getStatus()))
                .andExpect(jsonPath("code").value(ErrorCode.INVALID_VALIDATE.getCode()))
                .andExpect(jsonPath("message").value(Matchers.containsString("JSON parse error")))
                .andDo(ExceptionDocumentation.jacksonParsing());
    }

    @DisplayName("Pathvariable로 들어오는 타입이 맞지 않으면 예외를 반환한다.")
    @Test
    void methodArgumentTypeMismatchException() throws Exception {
        mockMvc.perform(get("/api/orders/{id}", "STRING")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(ErrorCode.INVALID_VALIDATE.getStatus()))
                .andExpect(jsonPath("code").value(ErrorCode.INVALID_VALIDATE.getCode()))
                .andExpect(jsonPath("message").value(Matchers.containsString("Failed to convert value")))
                .andDo(ExceptionDocumentation.methodArgumentTypeException());
    }

    @DisplayName("MediaType을 지원하지 않는 경우 400예외를 반환한다.")
    @Test
    void httpMediaTypeNotSupported() throws Exception {
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_ATOM_XML)
                .content(objectMapper.writeValueAsBytes(new OrderCreateRequestDto()))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(ErrorCode.NOT_SUPPORTED_MEDIA_TYPE.getStatus()))
                .andExpect(jsonPath("code").value(ErrorCode.NOT_SUPPORTED_MEDIA_TYPE.getCode()))
                .andDo(ExceptionDocumentation.httpMediaTypeNotSupported());
    }

    @DisplayName("서버에서 알 수 없는 예외를 반환하는 경우 500코드를 준다.")
    @Test
    void exception() throws Exception {
        when(orderService.fetchById(anyLong())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/orders/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("status").value(ErrorCode.UNEXPECTED.getStatus()))
                .andExpect(jsonPath("code").value(ErrorCode.UNEXPECTED.getCode()))
                .andDo(ExceptionDocumentation.unexpectedException());
    }
}
