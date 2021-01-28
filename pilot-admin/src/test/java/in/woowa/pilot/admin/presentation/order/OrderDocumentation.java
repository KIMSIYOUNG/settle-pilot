package in.woowa.pilot.admin.presentation.order;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static in.woowa.pilot.admin.presentation.DocumentFormat.*;
import static in.woowa.pilot.admin.presentation.DocumentUtils.getDocumentRequest;
import static in.woowa.pilot.admin.presentation.DocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

public class OrderDocumentation {

    public static RestDocumentationResultHandler create() {
        return document("orders/create",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("콘텐츠 타입")
                ),
                requestFields(
                        fieldWithPath("ownerId").type(NUMBER).description("주문을 받은 업주의 아이디"),
                        fieldWithPath("orderStatus").attributes(getOrderStatus()).description("주문 상태"),
                        fieldWithPath("orderDateTime").attributes(getDateTimeFormat()).description("주문된 시간"),
                        fieldWithPath("orderDetails").type(ARRAY).description("주문상세 - 결제정보"),
                        fieldWithPath("orderDetails[].paymentType").attributes(getPaymentType()).description("결제 타입"),
                        fieldWithPath("orderDetails[].paymentOption").attributes(getPaymentOption()).description("결제 타입 하위의 옵션(없으면 EMPTY)"),
                        fieldWithPath("orderDetails[].amount").type(NUMBER).description("결제 금액")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.LOCATION).description("생성된 리소스의 Location")
                )
        );
    }

    public static RestDocumentationResultHandler createReOrder() {
        return document("orders/create-reOrder",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("콘텐츠 타입")
                ),
                requestFields(
                        fieldWithPath("orderNo").type(STRING).description("기존 주문번호 - 재주문은 기존 주문번호를 사용합니다."),
                        fieldWithPath("orderStatus").attributes(getOrderStatus()).description("주문 상태"),
                        fieldWithPath("orderDateTime").attributes(getDateTimeFormat()).description("주문된 시간"),
                        fieldWithPath("orderDetails").type(ARRAY).description("주문상세 - 결제정보"),
                        fieldWithPath("orderDetails[].paymentType").attributes(getPaymentType()).description("결제 타입"),
                        fieldWithPath("orderDetails[].paymentOption").attributes(getPaymentOption()).description("결제 타입 하위의 옵션(없으면 EMPTY)"),
                        fieldWithPath("orderDetails[].amount").type(NUMBER).description("결제 금액")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.LOCATION).description("생성된 리소스의 Location")
                )
        );
    }

    public static RestDocumentationResultHandler fetchById() {
        return document("orders/fetch-by-id",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("id").description("조회하고자 하는 주문 ID")
                ),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("받는 데이터 타입")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type 헤더")
                ),
                responseFields(
                        fieldWithPath("id").type(NUMBER).description("주문 ID"),
                        fieldWithPath("orderNo").type(STRING).description("주문 번호"),
                        fieldWithPath("orderStatus").attributes(getOrderStatus()).description("주문 상태"),
                        fieldWithPath("orderDateTime").attributes(getDateTimeFormat()).description("주문된 시간"),
                        fieldWithPath("amount").type(NUMBER).description("주문 총 금액(주문상세의 합)"),
                        fieldWithPath("owner").type(OBJECT).description("판매한 업주정보"),
                        fieldWithPath("owner.id").type(NUMBER).description("판매한 업주의 ID"),
                        fieldWithPath("owner.name").type(STRING).description("판매한 업주의 이름"),
                        fieldWithPath("owner.email").attributes(getEmailFormat()).description("판매한 업주의 이메일"),
                        fieldWithPath("owner.settleType").type(STRING).description("판매한 업주의 지급금 유형"),
                        fieldWithPath("owner.account").type(OBJECT).description("판매한 업주의 계좌정보"),
                        fieldWithPath("owner.account.accountNumber").type(STRING).description("판매한 업주의 계좌번호"),
                        fieldWithPath("owner.account.accountType").type(STRING).description("판매한 업주의 계좌종류"),
                        fieldWithPath("orderDetails").type(OBJECT).description("주문상세 - 결제정보"),
                        fieldWithPath("orderDetails.orderDetails[].paymentType").attributes(getPaymentType()).description("결제 타입"),
                        fieldWithPath("orderDetails.orderDetails[].paymentOption").attributes(getPaymentOption()).description("결제 타입 하위의 옵션"),
                        fieldWithPath("orderDetails.orderDetails[].amount").type(NUMBER).description("결제 금액"),
                        fieldWithPath("orderSnapShots").type(OBJECT).description("주문에 대한 상태 변화"),
                        fieldWithPath("orderSnapShots.snapshots[].orderId").type(NUMBER).description("결제 금액"),
                        fieldWithPath("orderSnapShots.snapshots[].orderStatus").type(STRING).description("주문 상태"),
                        fieldWithPath("orderSnapShots.snapshots[].statusAt").type(STRING).description("주문 상태의 시점")
                )
        );
    }

    public static RestDocumentationResultHandler fetchByCondition() {
        return document("orders/fetch-by-condition",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("받는 데이터 타입")
                ),
                requestParameters(
                        parameterWithName("ownerId").optional().description("업주 ID를 기준으로 조회"),
                        parameterWithName("settleId").optional().description("지급금 ID를 기준으로 조회"),
                        parameterWithName("startAt").optional().attributes(getDateTimeFormat()).description("주문발생일자의 검색 시작시점"),
                        parameterWithName("endAt").optional().attributes(getDateTimeFormat()).description("주문발생일자의 검색 끝시점"),
                        parameterWithName("page").optional().description("검색시작 페이지"),
                        parameterWithName("size").optional().description("페이지당 컨텐츠의 갯수")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type 헤더")
                ),
                responseFields(
                        subsectionWithPath("orders").type(OBJECT).description("전체 정보"),
                        subsectionWithPath("orders.content").type(ARRAY).description("해당 페이지의 주문 컨텐츠"),
                        subsectionWithPath("orders.totalElements").type(NUMBER).description("총 컨텐츠의 갯수"),
                        subsectionWithPath("orders.pageable").type(OBJECT).description("페이지 관련 정보")
                )
        );
    }

    public static RestDocumentationResultHandler fetchBySettleId() {
        return document("orders/fetch-by-settle-id",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("settleId").description("기준이 되는 지급금 ID")
                ),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("받는 데이터 타입")
                ),
                requestParameters(
                        parameterWithName("ownerId").optional().description("업주 ID를 기준으로 조회"),
                        parameterWithName("settleId").optional().description("지급금 ID를 기준으로 조회"),
                        parameterWithName("startAt").optional().attributes(getDateTimeFormat()).description("주문발생일자의 검색 시작시점"),
                        parameterWithName("endAt").optional().attributes(getDateTimeFormat()).description("주문발생일자의 검색 끝시점"),
                        parameterWithName("page").optional().description("검색시작 페이지"),
                        parameterWithName("size").optional().description("페이지당 컨텐츠의 갯수")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type 헤더")
                ),
                responseFields(
                        subsectionWithPath("orders").type(OBJECT).description("전체 정보"),
                        subsectionWithPath("orders.content").type(ARRAY).description("해당 페이지의 주문 컨텐츠"),
                        subsectionWithPath("orders.totalElements").type(NUMBER).description("총 컨텐츠의 갯수"),
                        subsectionWithPath("orders.pageable").type(OBJECT).description("페이지 관련 정보")
                )
        );
    }

    public static RestDocumentationResultHandler updateStatus() {
        return document("orders/update-status",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보내는 데이터 타입")
                ),
                requestFields(
                        fieldWithPath("id").type(NUMBER).description("상태를 수정하고 싶은 주문의 ID"),
                        fieldWithPath("status").attributes(getOrderStatus()).description("변경하고자 하는 주문 상태")
                ),
                responseHeaders(
                        headerWithName("Location").description("수정된 리소스의 Location")
                )
        );
    }

    public static RestDocumentationResultHandler deleteById() {
        return document("orders/delete-by-id",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("id").description("삭제하고 싶은 주문의 ID")
                )
        );
    }
}
