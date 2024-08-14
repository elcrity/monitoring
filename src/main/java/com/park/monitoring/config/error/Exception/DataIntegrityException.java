package com.park.monitoring.config.error.Exception;

import com.park.monitoring.config.error.ErrorCode;
import org.springframework.dao.DataIntegrityViolationException;

public class DataIntegrityException extends DataIntegrityViolationException {
    private final ErrorCode errorCode;
    public DataIntegrityException(String message, ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public DataIntegrityException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getMessage(){
        return errorCode.getMessage();
    }
}
