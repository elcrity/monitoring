package com.park.monitoring.config.error.Exception;

import com.park.monitoring.config.error.ErrorCode;

import java.util.NoSuchElementException;

public class NotFoundException extends NoSuchElementException {
    ErrorCode errorCode;
    public NotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
