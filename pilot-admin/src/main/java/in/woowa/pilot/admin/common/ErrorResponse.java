package in.woowa.pilot.admin.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final int status;
    private final String code;
    private final String message;
    private final List<FieldError> errors;

    public ErrorResponse(int status, String code, String message, BindingResult result) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.errors = FieldError.listOf(result);
    }

    public ErrorResponse(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.errors = null;
    }

    @Getter
    @RequiredArgsConstructor
    public static class FieldError {
        private final String field;
        private final String value;
        private final String reason;

        private static List<FieldError> listOf(BindingResult result) {
            return result.getFieldErrors().stream()
                    .map(field -> new FieldError(
                            field.getField(),
                            field.getRejectedValue() == null ? "" : field.getRejectedValue().toString(),
                            field.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
}
