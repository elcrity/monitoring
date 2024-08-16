package com.park.monitoring.config.error.Exception;

import com.park.monitoring.config.error.ErrorCode;

public class NoContentException extends RuntimeException {
    ErrorCode errorCode;
    public NoContentException(ErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getMessage(){
        return errorCode.getMessage();
    }
}
