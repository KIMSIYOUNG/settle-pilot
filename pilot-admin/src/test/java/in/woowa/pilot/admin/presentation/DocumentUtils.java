package in.woowa.pilot.admin.presentation;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class DocumentUtils {

    public static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
                modifyUris()
                        .scheme("http")
                        .host("localhost")
                        .removePort(),
                prettyPrint()
        );
    }

    public static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(prettyPrint());
    }

    public static ResponseFieldsSnippet getErrorResponseFieldsWithFieldErrors() {
        return responseFields(
                fieldWithPath("status").description("에러 상태"),
                fieldWithPath("code").description("에러 코드"),
                fieldWithPath("message").description("에러 메세지"),
                subsectionWithPath("errors").description("필드 에러").optional()
        );
    }

    public static ResponseFieldsSnippet getErrorResponseFields() {
        return responseFields(
                fieldWithPath("status").description("에러 상태"),
                fieldWithPath("code").description("에러 코드"),
                fieldWithPath("message").description("에러 메세지")
        );
    }

}
