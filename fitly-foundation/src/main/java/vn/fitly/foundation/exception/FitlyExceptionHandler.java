package vn.fitly.foundation.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import vn.fitly.common.exception.ErrorStatus;
import vn.fitly.common.exception.FitlyRuntimeException;
import vn.fitly.common.language.DefaultSystemMessage;
import vn.fitly.foundation.response.BaseResponse;

@RestControllerAdvice
public class FitlyExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FitlyExceptionHandler.class);

    @ExceptionHandler(FitlyRuntimeException.class)
    public ResponseEntity<BaseResponse<Void>> handleFitlyRuntimeException(FitlyRuntimeException e) {
        HttpStatus status = HttpStatus.resolve(e.getStatus());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        if (status.is5xxServerError()) {
            LOGGER.error("Request failed with status={} errorCode={}", e.getStatus(), e.getErrorCode(), e);
        } else {
            LOGGER.debug("Request rejected with status={} errorCode={}", e.getStatus(), e.getErrorCode(), e);
        }

        return ResponseEntity.status(status)
                .body(BaseResponse.error(e.getErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleException(Exception e) {
        LOGGER.error("Unexpected request failure", e);

        return ResponseEntity.status(ErrorStatus.INTERNAL_ERROR.getStatus())
                .body(BaseResponse.error(DefaultSystemMessage.INTERNAL_ERROR.name()));
    }
}
