package in.woowa.pilot.admin.acceptance;

import in.woowa.pilot.admin.application.order.dto.response.OrderResponseDto;
import in.woowa.pilot.admin.application.order.dto.response.OrderResponsesDto;
import in.woowa.pilot.admin.presentation.order.dto.request.OrderCreateRequestDto;
import in.woowa.pilot.admin.presentation.order.dto.request.OrderStatusUpdateRequestDto;
import in.woowa.pilot.admin.util.Orders;
import in.woowa.pilot.admin.util.Owners;
import in.woowa.pilot.core.order.OrderStatus;
import in.woowa.pilot.fixture.order.OrderFixture;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.util.LinkedMultiValueMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderAcceptanceTest extends ApiCall {

    /**
     * 주문 관리
     * <p>
     * 1. 관리자를 생성한다.
     * 2. 업주를 생성한다.
     * 3. 주문을 등록할 수 있다. (4일전 주문)
     * 4. 주문을 조회한다.
     * 5. 주문의 상태를 수정한다.
     * 6. 주문을 삭제한다.
     * 7. 주문을 3건 추가한다. (3일전, 2일전, 1일전 주문)
     * 8. 주문을 모두 조회한다.
     */
    @DisplayName("주문 인수테스트(해피 케이스) - 주문에 대한 API를 관리한다.")
    @Test
    void manageOrder() throws Exception {
        Runnable 관리자회원 = 관리자회원세션();

        String 생성된업주_리소스 = 데이터생성요청(관리자회원, Owners.webCreateRequestDto(), "/api/owners");
        Long 생성된업주_아이디 = getIdBy(생성된업주_리소스);
        OrderCreateRequestDto 해당업주의_주문요청데이터 = Orders.webOrderCreateRequest(생성된업주_아이디, 2, LocalDateTime.now().minusDays(4), OrderStatus.ORDER);

        String 생성된주문_리소스 = 데이터생성요청(관리자회원, 해당업주의_주문요청데이터, OrderFixture.RESOURCE);
        OrderResponseDto 받아온_주문데이터 = 데이터요청(관리자회원, OrderResponseDto.class, OrderFixture.RESOURCE + getPathVariableBy(생성된주문_리소스));
        주문검증_요청데이터_실제데이터(받아온_주문데이터, 해당업주의_주문요청데이터);

        OrderStatusUpdateRequestDto 주문취소요청데이터 = Orders.webOrderStatusUpdateDto(받아온_주문데이터.getId(), OrderStatus.DELIVERY);
        데이터_수정_요청(관리자회원, 주문취소요청데이터, OrderFixture.RESOURCE);
        OrderResponseDto 받아온_수정된주문데이터 = 데이터요청(관리자회원, OrderResponseDto.class, OrderFixture.RESOURCE + getPathVariableBy(생성된주문_리소스));
        수정검증_수정요청데이터_실제데이터(받아온_수정된주문데이터, 주문취소요청데이터);

        데이터삭제요청(관리자회원, OrderFixture.RESOURCE + getPathVariableBy(생성된주문_리소스));
        데이서요청및_응답코드확인(관리자회원, status().isNotFound(), OrderFixture.RESOURCE + getPathVariableBy(생성된주문_리소스));

        벌크성_추가주문데이터저장(
                관리자회원,
                Orders.webOrderCreateRequest(생성된업주_아이디, 2, LocalDateTime.of(LocalDate.now().minusDays(3), LocalTime.MIDNIGHT)),
                Orders.webOrderCreateRequest(생성된업주_아이디, 2, LocalDateTime.of(LocalDate.now().minusDays(2), LocalTime.MIDNIGHT)),
                Orders.webOrderCreateRequest(생성된업주_아이디, 2, LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIDNIGHT))
        );

        LinkedMultiValueMap<String, String> 검색조건 = new LinkedMultiValueMap<>();
        검색조건.add("startAt", LocalDateTime.now().minusDays(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        검색조건.add("endAt", LocalDateTime.now().minusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        검색조건.add("page", "0");
        검색조건.add("size", "3");
        OrderResponsesDto 받아온_추가주문데이터 = 데이터요청(관리자회원, OrderResponsesDto.class, OrderFixture.RESOURCE, 검색조건);
        주문검증(받아온_추가주문데이터);
    }

    private void 주문검증_요청데이터_실제데이터(OrderResponseDto orderApiResponseDto, OrderCreateRequestDto orderRequest) {
        assertAll(
                () -> assertThat(orderApiResponseDto.getOrderDetails().getOrderDetails()).hasSize(2),
                () -> assertThat(orderApiResponseDto.getOrderDateTime()).isCloseTo(orderRequest.getOrderDateTime(), new TemporalUnitWithinOffset(1, ChronoUnit.MINUTES))
        );
    }

    private void 수정검증_수정요청데이터_실제데이터(OrderResponseDto updatedOrderResponse, OrderStatusUpdateRequestDto updateRequest) {
        assertThat(updatedOrderResponse.getOrderStatus()).isEqualTo(updateRequest.getStatus().name());
    }

    private void 주문검증(OrderResponsesDto orderApiResponsesDto) {
        Page<OrderResponseDto> orders = orderApiResponsesDto.getOrders();
        assertThat(orders.getTotalElements()).isEqualTo(2);
    }

    private void 벌크성_추가주문데이터저장(Runnable session, OrderCreateRequestDto... requests) throws Exception {
        for (OrderCreateRequestDto requestDto : requests) {
            데이터생성요청(session, requestDto, OrderFixture.RESOURCE);
        }
    }
}
