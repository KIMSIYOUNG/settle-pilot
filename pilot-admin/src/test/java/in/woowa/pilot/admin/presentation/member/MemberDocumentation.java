package in.woowa.pilot.admin.presentation.member;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static in.woowa.pilot.admin.presentation.DocumentFormat.*;
import static in.woowa.pilot.admin.presentation.DocumentUtils.getDocumentRequest;
import static in.woowa.pilot.admin.presentation.DocumentUtils.getDocumentResponse;

public class MemberDocumentation {

    public static RestDocumentationResultHandler findByCurrentMember() {
        return document("member/get-success",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더")
                ),
                responseFields(
                        fieldWithPath("id").type(NUMBER).description("회원의 아이디"),
                        fieldWithPath("name").type(STRING).description("회원의 이름"),
                        fieldWithPath("email").attributes(getEmailFormat()).description("회원이 가입한 이메일"),
                        fieldWithPath("provider").attributes(getAuthProvider()).description("가입한 OAuth2 채널"),
                        fieldWithPath("role").attributes(getMemberRoleFormat()).description("회원의 권한(일반회원/관리자)")
                )
        );
    }

    public static RestDocumentationResultHandler fetchAll() {
        return document("member/fetch-all",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더")
                ),
                responseFields(
                        subsectionWithPath("members").type(OBJECT).description("전체 정보"),
                        subsectionWithPath("members.content").type(ARRAY).description("해당 페이지의 주문 컨텐츠"),
                        subsectionWithPath("members.totalElements").type(NUMBER).description("총 컨텐츠의 갯수"),
                        subsectionWithPath("members.pageable").type(OBJECT).description("페이지 관련 정보"),

                        subsectionWithPath("members.content[].authorityId").type(NUMBER).description("해당 회원이 신청한 권한 ID"),
                        subsectionWithPath("members.content[].target").type(STRING).description("해당 회원이 신청한 권한"),
                        subsectionWithPath("members.content[].memberId").type(NUMBER).description("회원의 ID"),
                        subsectionWithPath("members.content[].name").type(STRING).description("회원의 이름"),
                        subsectionWithPath("members.content[].email").type(STRING).description("회원의 이메일"),
                        subsectionWithPath("members.content[].provider").type(STRING).description("회원의 가입경로"),
                        subsectionWithPath("members.content[].role").type(STRING).description("회원의 현재 역할")
                )
        );
    }

    public static RestDocumentationResultHandler changeMemberRole() {
        return document("member/change-member-role",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("memberId").description("변경하고자 하는 회원의 ID"),
                        fieldWithPath("role").description("변경할 역할")
                )
        );
    }

    public static RestDocumentationResultHandler deleteByAdmin() {
        return document("member/delete-by-admin",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("id").description("회원탈퇴시키고자 하는 회원의 ID")
                )
        );
    }

    public static RestDocumentationResultHandler deleteByCurrentMember() {
        return document("member/delete-by-current-member",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }
}
