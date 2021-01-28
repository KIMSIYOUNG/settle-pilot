package in.woowa.pilot.admin.presentation.order;

import in.woowa.pilot.admin.application.order.OrderService;
import in.woowa.pilot.admin.presentation.DocsWithSecuritySetup;
import in.woowa.pilot.admin.presentation.order.dto.request.OrderCancelReOrderRequestDto;
import in.woowa.pilot.admin.util.Orders;
import in.woowa.pilot.core.config.QueryDslConfiguration;
import in.woowa.pilot.core.order.OrderStatus;
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

@WebMvcTest(controllers = OrderController.class, excludeAutoConfiguration = QueryDslConfiguration.class)
class OrderControllerTest extends DocsWithSecuritySetup {
    private Owner owner;

    @MockBean
    OrderService orderService;

    @BeforeEach
    void setUp() {
        owner = OwnerFixture.createWithId();
    }

    @DisplayName("주문을 정상적으로 생성할 수 있다.")
    @Test
    void create() throws Exception {
        when(orderService.create(any())).thenReturn(Orders.createResponseDto(owner));

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Orders.webOrderCreateRequest(owner, 2)))
        )
                .andExpect(status().isCreated())
                .andDo(OrderDocumentation.create());
    }

    @DisplayName("주문을 취소하고 해당 주문과 관련된 주문을 생성할 수 있다.")
    @Test
    void reOrder() throws Exception {
        when(orderService.cancelAndReOrder(any())).thenReturn(Orders.createResponseDto(owner));
        OrderCancelReOrderRequestDto request = Orders.webReOrderCreateRequest(2);

        mockMvc.perform(post("/api/orders/re-order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isCreated())
                .andDo(OrderDocumentation.createReOrder());
    }

    @DisplayName("주문 ID로 주문을 조회한다.")
    @Test
    void fetchById() throws Exception {
        when(orderService.fetchById(anyLong())).thenReturn(Orders.createResponseDto(owner));

        mockMvc.perform(get("/api/orders/{id}", 2)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(OrderDocumentation.fetchById());
    }

    @DisplayName("조건에 따라 주문을 조회할 수 있다.")
    @Test
    void fetchByCondition() throws Exception {
        when(orderService.fetchPagedByCondition(any(), any())).thenReturn(Orders.createPagedResponsesDto(owner));

        mockMvc.perform(get("/api/orders")
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(OrderDocumentation.fetchByCondition());
    }

    @DisplayName("정산 번호로 주문을 조회할 수 있다.")
    @Test
    void fetchBySettleId() throws Exception {
        when(orderService.fetchBySettleId(anyLong(), any())).thenReturn(Orders.createPagedResponsesDto(owner));

        mockMvc.perform(get("/api/orders/settles/{settleId}", 1)
                .param("page", "0")
                .param("size", "10")

                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(OrderDocumentation.fetchBySettleId());
    }

    @DisplayName("주문의 상태를 변경할 수 있다.")
    @Test
    void updateOrderStatus() throws Exception {
        doNothing().when(orderService).updateOrderStatus(any());

        mockMvc.perform(patch("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(Orders.webOrderStatusUpdateDto(2L, OrderStatus.DELIVERY)))
        )
                .andExpect(status().isNoContent())
                .andDo(OrderDocumentation.updateStatus());
    }

    @DisplayName("주문을 삭제할 수 있다.")
    @Test
    void deleteById() throws Exception {
        doNothing().when(orderService).delete(anyLong());

        mockMvc.perform(delete("/api/orders/{id}", 2))
                .andExpect(status().isNoContent())
                .andDo(OrderDocumentation.deleteById());
    }
}