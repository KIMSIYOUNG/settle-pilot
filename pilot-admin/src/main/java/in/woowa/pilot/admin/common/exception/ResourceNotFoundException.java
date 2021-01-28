package in.woowa.pilot.admin.common.exception;

import in.woowa.pilot.admin.common.ErrorCode;
import lombok.Getter;

@Getter
public class ResourceNotFoundException extends BusinessException {
    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(ErrorCode.RESOURCE_NOT_FOUND, String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
