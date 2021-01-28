package in.woowa.pilot.admin.common.exception;

import in.woowa.pilot.admin.common.ErrorCode;
import lombok.Getter;

@Getter
public class ResourceInvalidException extends BusinessException {
    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;
    private final String detail;

    public ResourceInvalidException(String resourceName, String fieldName, Object fieldValue, String detail) {
        super(ErrorCode.RESOURCE_INVALID, String.format("%s is invalid! %s : '%s'" + detail, resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.detail = detail;
    }
}
