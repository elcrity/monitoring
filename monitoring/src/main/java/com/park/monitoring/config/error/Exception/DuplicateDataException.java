package com.park.monitoring.config.error.Exception;

import com.park.monitoring.config.error.ErrorCode;
import org.springframework.dao.DataIntegrityViolationException;

public class DuplicateDataException extends DataIntegrityViolationException {
    ErrorCode errorCode;
    public DuplicateDataException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
