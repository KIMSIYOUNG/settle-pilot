package in.woowa.pilot.admin.presentation.reward;

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

public class RewardDocumentation {

    public static RestDocumentationResultHandler create() {
        return document("rewards/create",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("콘텐츠 타입")
                ),
                requestFields(
                        fieldWithPath("amount").type(NUMBER).description("보상금액 액수"),
                        fieldWithPath("rewardType").attributes(getRewardType()).description("보상금액 타입"),
                        fieldWithPath("description").type(STRING).description("구체적인 보상의 이유"),
                        fieldWithPath("ownerId").type(NUMBER).description("보상받을 업주의 ID"),
                        fieldWithPath("rewardDateTime").attributes(getDateTimeFormat()).description("보상금 지급을 신청한 시점")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.LOCATION).description("생성된 리소스의 Location")
                )
        );
    }

    public static RestDocumentationResultHandler createByPeriod() {
        return document("rewards/create/period",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("콘텐츠 타입")
                ),
                requestFields(
                        fieldWithPath("rewardType").attributes(getRewardType()).description("보상금액 타입"),
                        fieldWithPath("description").type(STRING).description("구체적인 보상의 이유"),
                        fieldWithPath("rewardDateTime").attributes(getDateTimeFormat()).description("보상금 지급을 신청한 시점"),
                        fieldWithPath("startAt").attributes(getDateTimeFormat()).description("보상을 지불할 시작 시점"),
                        fieldWithPath("endAt").attributes(getDateTimeFormat()).description("보상을 종료할 종료 시점")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.LOCATION).description("생성된 리소스의 Location")
                )
        );
    }

    public static RestDocumentationResultHandler createByOrders() {
        return document("rewards/create/orders",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("콘텐츠 타입")
                ),
                requestFields(
                        fieldWithPath("rewardType").attributes(getRewardType()).description("보상금액 타입"),
                        fieldWithPath("description").type(STRING).description("구체적인 보상의 이유"),
                        fieldWithPath("rewardDateTime").attributes(getDateTimeFormat()).description("보상금 지급을 신청한 시점"),
                        fieldWithPath("orderIds").type(ARRAY).description("보상할 주문의 번호들(주문 ID 목록)")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.LOCATION).description("생성된 리소스의 Location")
                )
        );
    }

    public static RestDocumentationResultHandler fetchById() {
        return document("rewards/fetch-by-id",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("id").description("찾고자 하는 보상금액의 ID")
                ),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type 헤더")
                ),
                responseFields(
                        fieldWithPath("id").type(NUMBER).description("업주의 아이디"),
                        fieldWithPath("rewardNo").type(STRING).description("생성된 주문 번호"),
                        fieldWithPath("amount").type(NUMBER).description("보상금액 액수"),
                        fieldWithPath("rewardType").attributes(getRewardType()).description("보상금액의 타입"),
                        fieldWithPath("description").type(STRING).description("구체적인 보상 이유"),
                        fieldWithPath("owner").type(OBJECT).description("보상 받을 업주 정보"),
                        fieldWithPath("owner.id").type(NUMBER).description("보상 받을 업주의 ID"),
                        fieldWithPath("owner.name").type(STRING).description("보상 받을 업주의 이름"),
                        fieldWithPath("owner.email").attributes(getEmailFormat()).description("보상 받을 업주의 이메일"),
                        fieldWithPath("owner.settleType").type(STRING).description("판매한 업주의 지급금 유형"),
                        fieldWithPath("owner.account").type(OBJECT).description("판매한 업주의 계좌정보"),
                        fieldWithPath("owner.account.accountNumber").type(STRING).description("판매한 업주의 계좌번호"),
                        fieldWithPath("owner.account.accountType").type(STRING).description("판매한 업주의 계좌종류"),
                        fieldWithPath("rewardDateTime").attributes(getDateTimeFormat()).description("보상금액 생성시점"),
                        fieldWithPath("order").optional().type(OBJECT).description("연관된 주문 - 없으면 null로 표시")
                )
        );
    }

    public static RestDocumentationResultHandler findAll() {
        return document("rewards/fetch-all",
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
                        subsectionWithPath("rewards").type(OBJECT).description("전체 정보"),
                        subsectionWithPath("rewards.content").type(ARRAY).description("해당 페이지의 업주 컨텐츠"),
                        subsectionWithPath("rewards.totalElements").type(NUMBER).description("총 컨텐츠의 갯수"),
                        subsectionWithPath("rewards.pageable").type(OBJECT).description("페이지 관련 정보")
                )
        );
    }

    public static RestDocumentationResultHandler fetchByCondition() {
        return document("rewards/fetch-by-condition",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("ownerName").optional().description("조회하고자 하는 업주의 이름"),
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
                        subsectionWithPath("rewards").type(OBJECT).description("전체 정보"),
                        subsectionWithPath("rewards.content").type(ARRAY).description("해당 페이지의 업주 컨텐츠"),
                        subsectionWithPath("rewards.totalElements").type(NUMBER).description("총 컨텐츠의 갯수"),
                        subsectionWithPath("rewards.pageable").type(OBJECT).description("페이지 관련 정보")
                )
        );
    }

    public static RestDocumentationResultHandler fetchBySettleId() {
        return document("rewards/fetch-by-settle-id",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("settleId").description("기준이 되는 지급금 ID")
                ),
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
                        subsectionWithPath("rewards").type(OBJECT).description("전체 정보"),
                        subsectionWithPath("rewards.content").type(ARRAY).description("해당 페이지의 업주 컨텐츠"),
                        subsectionWithPath("rewards.totalElements").type(NUMBER).description("총 컨텐츠의 갯수"),
                        subsectionWithPath("rewards.pageable").type(OBJECT).description("페이지 관련 정보")
                )
        );
    }

    public static RestDocumentationResultHandler update() {
        return document("rewards/update",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 컨텐츠 타입")
                ),
                requestFields(
                        fieldWithPath("id").type(NUMBER).description("삭제하고자 하는 보상금액 ID"),
                        fieldWithPath("amount").type(NUMBER).description("수정할 금액"),
                        fieldWithPath("rewardType").attributes(getRewardType()).description("수정할 보상금액 타입"),
                        fieldWithPath("description").type(STRING).description("수정할 상세정보")
                ),
                responseHeaders(
                        headerWithName("Location").description("변경된 보상금액의 리소스")
                )
        );
    }

    public static RestDocumentationResultHandler delete() {
        return document("rewards/delete",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("id").description("삭제하고자 하는 보상금액 ID")
                ),
                pathParameters(
                        parameterWithName("id").description("삭제하고 싶은 업주의 ID")
                )
        );
    }
}
