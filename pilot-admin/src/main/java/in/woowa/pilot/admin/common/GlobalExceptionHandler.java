package in.woowa.pilot.admin.common;

import in.woowa.pilot.admin.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> validException(MethodArgumentNotValidException exception) {
        log.info("Validate Exception ! : {} ", exception.toString(), exception);

        ErrorCode errorCode = ErrorCode.INVALID_VALIDATE;
        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.getStatus(),
                errorCode.getCode(),
                "Argument Invalid",
                exception.getBindingResult()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> bindException(BindException exception) {
        log.info("HandleBind Exception ! : {} ", exception.toString(), exception);

        ErrorCode errorCode = ErrorCode.INVALID_VALIDATE;
        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.getStatus(),
                errorCode.getCode(),
                "Invalid Binding please check the search conditions",
                exception.getBindingResult()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    protected ResponseEntity<ErrorResponse> invalidInputException(RuntimeException exception) {
        log.info(exception.getClass().getSimpleName() + " Exception !", exception);

        ErrorCode errorCode = ErrorCode.INVALID_VALIDATE;
        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.getStatus(),
                errorCode.getCode(),
                exception.getMessage()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> invalidInputException(HttpMediaTypeNotSupportedException exception) {
        log.info(exception.getClass().getSimpleName() + " Exception !", exception);

        ErrorCode errorCode = ErrorCode.NOT_SUPPORTED_MEDIA_TYPE;
        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.getStatus(),
                errorCode.getCode(),
                exception.getMessage()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> businessException(BusinessException exception) {
        log.info("Business Exception ! : {}", exception.toString(), exception);

        ErrorCode errorCode = exception.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.getStatus(),
                errorCode.getCode(),
                exception.getMessage()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorResponse> UnexpectedException(IllegalArgumentException exception) {
        log.info("Unexpected Exception ! {} ", exception.toString(), exception);

        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.INVALID_VALIDATE.getStatus(),
                ErrorCode.INVALID_VALIDATE.getCode(),
                exception.getMessage()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ErrorCode.INVALID_VALIDATE.getStatus()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ErrorResponse> dataIntegrityViolationException(DataIntegrityViolationException exception) {
        log.info("Unexpected Exception ! {} ", exception.toString(), exception);

        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.INVALID_VALIDATE.getStatus(),
                ErrorCode.INVALID_VALIDATE.getCode(),
                "이메일은 중복될 수 없습니다."
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ErrorCode.INVALID_VALIDATE.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> UnexpectedException(Exception exception) {
        log.error("Unexpected Exception ! {} ", exception.toString(), exception);

        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.UNEXPECTED.getStatus(),
                ErrorCode.UNEXPECTED.getCode(),
                exception.getMessage()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ErrorCode.UNEXPECTED.getStatus()));
    }
}
