package in.woowa.pilot.admin.presentation.authority;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static in.woowa.pilot.admin.presentation.DocumentFormat.getMemberRoleFormat;
import static in.woowa.pilot.admin.presentation.DocumentUtils.getDocumentRequest;
import static in.woowa.pilot.admin.presentation.DocumentUtils.getDocumentResponse;

public class AuthorityDocumentation {
    public static RestDocumentationResultHandler create() {
        return document("authorities/create",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 Content-type")
                ),
                requestFields(
                        fieldWithPath("memberId").description("권한을 요청한 회원의 ID"),
                        fieldWithPath("role").attributes(getMemberRoleFormat()).description("변경될 권한")
                )
        );
    }

    public static RestDocumentationResultHandler approve() {
        return document("authorities/approve",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("id").description("권한요청한 권한 도메인의 ID")
                )
        );
    }

    public static RestDocumentationResultHandler reject() {
        return document("authorities/reject",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("id").description("권한요청한 권한 도메인의 ID")
                )
        );
    }
}
