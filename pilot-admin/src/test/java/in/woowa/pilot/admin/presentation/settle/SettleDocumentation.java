package in.woowa.pilot.admin.presentation.settle;

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

public class SettleDocumentation {

    public static RestDocumentationResultHandler create() {
        return document("settles/create",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("콘텐츠 타입")
                ),
                requestFields(
                        fieldWithPath("ownerId").type(NUMBER).description("지급금 신청할 업주의 ID"),
                        fieldWithPath("settleType").attributes(getSettleType()).description("지급금 단위(일/주/월)"),
                        fieldWithPath("criteriaDate").type(STRING).description("기준 일자")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.LOCATION).description("생성된 리소스의 Location")
                )
        );
    }

    public static RestDocumentationResultHandler createBatch() {
        return document("settles/create/batch",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("콘텐츠 타입")
                ),
                requestFields(
                        fieldWithPath("settleType").attributes(getSettleType()).description("지급금 단위(일/주/월)"),
                        fieldWithPath("criteriaDate").type(STRING).description("기준 일자")
                )
        );
    }

    public static RestDocumentationResultHandler fetchByCondition() {
        return document("settles/fetch-by-condition",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("page").optional().description("조회하고자 하는 페이지 (0부터 시작)"),
                        parameterWithName("size").optional().description("한 페이지당 갯수"),
                        parameterWithName("ownerId").optional().description("지급금을 받을 업주의 ID"),
                        parameterWithName("startAt").optional().attributes(getDateTimeFormat()).description("지급금 시작점"),
                        parameterWithName("endAt").optional().attributes(getDateTimeFormat()).description("지급금 끝점"),
                        parameterWithName("settleStatus").optional().attributes(getSettleStatus()).description("지급금 상태/타입"),
                        parameterWithName("settleType").optional().attributes(getSettleType()).description("지급금 유형")
                ),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type 헤더")
                ),
                responseFields(
                        subsectionWithPath("settles").type(OBJECT).description("전체 정보"),
                        subsectionWithPath("settles.content").type(ARRAY).description("해당 페이지의 업주 컨텐츠"),
                        subsectionWithPath("settles.totalElements").type(NUMBER).description("총 컨텐츠의 갯수"),
                        subsectionWithPath("settles.pageable").type(OBJECT).description("페이지 관련 정보")
                ));
    }

    public static RestDocumentationResultHandler fetchAll() {
        return document("settles/fetch-all",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("page").optional().description("조회하고자 하는 페이지 (0부터 시작)"),
                        parameterWithName("size").optional().description("한 페이지당 갯수")
                ),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type 헤더")
                ),
                responseFields(
                        subsectionWithPath("settles").type(OBJECT).description("전체 정보"),
                        subsectionWithPath("settles.content").type(ARRAY).description("해당 페이지의 업주 컨텐츠"),
                        subsectionWithPath("settles.totalElements").type(NUMBER).description("총 컨텐츠의 갯수"),
                        subsectionWithPath("settles.pageable").type(OBJECT).description("페이지 관련 정보")
                ));
    }

    public static RestDocumentationResultHandler fetchAmount() {
        return document("settles/fetch-amount-by-condition",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("startAt").optional().attributes(getDateTimeFormat()).description("지급금 시작점"),
                        parameterWithName("endAt").optional().attributes(getDateTimeFormat()).description("지급금 끝점")
                ),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type 헤더")
                ),
                responseFields(
                        subsectionWithPath("amount").type(NUMBER).description("총 지급받을 금액"),
                        subsectionWithPath("startAt").type(STRING).description("지급금 기간 시작점"),
                        subsectionWithPath("endAt").type(STRING).description("지급금 기간 끝점")
                ));
    }

    public static RestDocumentationResultHandler update() {
        return document("settles/update",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 컨텐츠 타입")
                ),
                requestFields(
                        fieldWithPath("id").description("정산완료 처리할 지급금의 ID"),
                        fieldWithPath("settleStatus").attributes(getSettleStatus()).description("수정할 지급금 타입")
                ),
                responseHeaders(
                        headerWithName("Location").description("변경된 보상금액의 리소스")
                )
        );
    }

    public static RestDocumentationResultHandler updateBulk() {
        return document("settles/update-bulk",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("ownerId").type(NUMBER).description("업주 ID"),
                        fieldWithPath("settleIds").type(ARRAY).description("지급완료 처리 할 지급금 ID들"),
                        fieldWithPath("startAt").type(STRING).description("지급완료를 시작할 시작일"),
                        fieldWithPath("endAt").type(STRING).description("지급완료를 종료할 종료일")
                )
        );
    }

    public static RestDocumentationResultHandler delete() {
        return document("settles/delete",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("id").description("삭제하고자 하는 지급금 ID")
                )
        );
    }
}
