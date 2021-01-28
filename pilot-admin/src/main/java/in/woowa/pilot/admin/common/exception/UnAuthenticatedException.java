package in.woowa.pilot.admin.common.exception;

import in.woowa.pilot.admin.common.ErrorCode;
import lombok.Getter;

@Getter
public class UnAuthenticatedException extends BusinessException {
    private final String fieldName;
    private final Object fieldValue;
    private final String detail;

    public UnAuthenticatedException(String fieldName, Object fieldValue, String detail) {
        super(ErrorCode.UN_AUTHENTICATE, String.format(" %s : '%s' is invalid! \n", fieldName, fieldValue) + detail);
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.detail = detail;
    }
}
