package in.woowa.pilot.admin.common.exception;

import in.woowa.pilot.admin.common.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(final ErrorCode errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
