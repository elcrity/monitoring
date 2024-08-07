package com.park.monitoring.config.error.Exception;

import com.park.monitoring.config.error.ErrorCode;

public class BaseException extends RuntimeException{

    private final ErrorCode errorCode;

    public BaseException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
