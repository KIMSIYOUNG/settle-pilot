package in.woowa.pilot.admin.presentation;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static in.woowa.pilot.admin.presentation.DocumentUtils.*;

public class ExceptionDocumentation {

    public static RestDocumentationResultHandler typeBindException() {
        return document("exception/path-bind-exception",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("응답 반환 타입")
                ),
                requestParameters(
                        parameterWithName("ownerId").description("업주 ID")
                ),
                getErrorResponseFieldsWithFieldErrors()
        );
    }

    public static RestDocumentationResultHandler datetimeBindException() {
        return document("exception/datetime-bind-exception",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("응답 반환 타입")
                ),
                requestParameters(
                        parameterWithName("startAt").description("시작일자")
                ),
                getErrorResponseFieldsWithFieldErrors()
        );
    }

    public static RestDocumentationResultHandler validException() {
        return document("exception/valid-exception",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 타입")
                ),
                getErrorResponseFieldsWithFieldErrors()
        );
    }

    public static RestDocumentationResultHandler jacksonParsing() {
        return document("exception/jackson-exception",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 타입")
                ),
                getErrorResponseFields()
        );
    }

    public static RestDocumentationResultHandler methodArgumentTypeException() {
        return document("exception/method-argument-type-exception",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("id").description("조회하고자 하는 업주 ID")
                ),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("응답 반환 타입")
                ),
                getErrorResponseFields()
        );
    }

    public static RestDocumentationResultHandler httpMediaTypeNotSupported() {
        return document("exception/media-type-not-supported-exception",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 타입")
                ),
                getErrorResponseFields()
        );
    }

    public static RestDocumentationResultHandler unexpectedException() {
        return document("exception/unexpected",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("id").description("조회하고자 하는 업주 ID")
                ),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("응답 반환 타입")
                ),
                responseFields(
                        fieldWithPath("status").description("에러 상태"),
                        fieldWithPath("code").description("에러 코드")
                )
        );
    }
}
