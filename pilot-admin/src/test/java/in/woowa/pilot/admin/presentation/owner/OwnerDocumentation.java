package in.woowa.pilot.admin.presentation.owner;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static in.woowa.pilot.admin.presentation.DocumentFormat.getEmailFormat;
import static in.woowa.pilot.admin.presentation.DocumentFormat.getSettleType;
import static in.woowa.pilot.admin.presentation.DocumentUtils.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

public class OwnerDocumentation {

    public static RestDocumentationResultHandler create() {
        return document("owners/create",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("콘텐츠 타입")
                ),
                requestFields(
                        fieldWithPath("name").type(STRING).description("업주의 이름"),
                        fieldWithPath("email").attributes(getEmailFormat()).description("업주의 이메일"),
                        fieldWithPath("settleType").attributes(getSettleType()).description("업주의 지급금 유형"),
                        fieldWithPath("account").type(OBJECT).description("업주의 계좌정보"),
                        fieldWithPath("account.accountType").type(STRING).description("업주의 계좌 종류"),
                        fieldWithPath("account.accountNumber.accountNumber").type(STRING).description("업주의 계좌번호")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.LOCATION).description("생성된 리소스의 Location")
                )
        );
    }

    public static RestDocumentationResultHandler createFail() {
        return document("owners/create-fail",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("콘텐츠 타입")
                ),
                requestFields(
                        fieldWithPath("name").description("업주의 이름"),
                        fieldWithPath("email").description("업주의 이메일"),
                        fieldWithPath("settleType").attributes(getSettleType()).description("해당 업주의 지급금 주기"),
                        fieldWithPath("account").type(OBJECT).optional().description("업주의 계좌정보")
                ),
                getErrorResponseFieldsWithFieldErrors()
        );
    }

    public static RestDocumentationResultHandler createForbidden() {
        return document("owners/create-forbidden",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("콘텐츠 타입")
                ),
                requestFields(
                        fieldWithPath("name").description("업주의 이름"),
                        fieldWithPath("email").description("업주의 이메일"),
                        fieldWithPath("settleType").description("업주의 지급금 유형"),
                        fieldWithPath("accountDto").type(OBJECT).description("업주의 계좌정보"),
                        fieldWithPath("accountDto.accountType").type(STRING).description("업주의 계좌 종류"),
                        fieldWithPath("accountDto.accountNumber.accountNumber").type(STRING).description("업주의 계좌번호")
                )
        );
    }

    public static RestDocumentationResultHandler findById() {
        return document("owners/get",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("id").description("찾고자 하는 업주의 ID")
                ),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type 헤더")
                ),
                responseFields(
                        fieldWithPath("id").type(NUMBER).description("업주의 아이디"),
                        fieldWithPath("name").type(STRING).description("업주의 이름"),
                        fieldWithPath("email").type(STRING).attributes(getEmailFormat()).description("업주가 가입한 이메일"),
                        fieldWithPath("settleType").type(STRING).description("업주의 지급금 유형"),
                        fieldWithPath("account").type(OBJECT).description("판매한 업주의 계좌정보"),
                        fieldWithPath("account.accountNumber").type(STRING).description("판매한 업주의 계좌번호"),
                        fieldWithPath("account.accountType").type(STRING).description("판매한 업주의 계좌종류")
                )
        );
    }

    public static RestDocumentationResultHandler findByNotExistId() {
        return document("owners/get-fail",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("id").description("찾고자 하는 업주의 ID")
                ),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type 헤더")
                ),
                getErrorResponseFields()
        );
    }

    public static RestDocumentationResultHandler findByCondition() {
        return document("owners/get-by-condition",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("ownerId").optional().description("조회하고자 하는 업주의 아이디"),
                        parameterWithName("name").optional().description("조회하고자 하는 업주의 이름"),
                        parameterWithName("email").optional().attributes(getEmailFormat()).description("조회하고자 하는 업주의 이메일"),
                        parameterWithName("settleType").optional().attributes(getSettleType()).description("조회하고자 하는 업주의 지급금 유형"),
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
                        subsectionWithPath("owners").type(OBJECT).description("전체 정보"),
                        subsectionWithPath("owners.content").type(ARRAY).description("해당 페이지의 업주 컨텐츠"),
                        subsectionWithPath("owners.totalElements").type(NUMBER).description("총 컨텐츠의 갯수"),
                        subsectionWithPath("owners.pageable").type(OBJECT).description("페이지 관련 정보")
                )
        );
    }

    public static RestDocumentationResultHandler findAll() {
        return document("owners/get-all",
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
                        subsectionWithPath("owners").type(OBJECT).description("전체 정보"),
                        subsectionWithPath("owners.content").type(ARRAY).description("해당 페이지의 업주 컨텐츠"),
                        subsectionWithPath("owners.totalElements").type(NUMBER).description("총 컨텐츠의 갯수"),
                        subsectionWithPath("owners.pageable").type(OBJECT).description("페이지 관련 정보")
                )
        );
    }

    public static RestDocumentationResultHandler update() {
        return document("owners/update",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 컨텐츠 타입")
                ),
                requestFields(
                        fieldWithPath("id").type(NUMBER).description("수정할 업주의 ID"),
                        fieldWithPath("name").type(STRING).description("수정하고 싶은 이름"),
                        fieldWithPath("email").attributes(getEmailFormat()).description("수정하고 싶은 이메일"),
                        fieldWithPath("settleType").attributes(getSettleType()).description("수정할 지급금 방식")
                )
        );
    }

    public static RestDocumentationResultHandler delete() {
        return document("owners/delete",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("id").description("삭제하고 싶은 업주의 ID")
                )
        );
    }
}
