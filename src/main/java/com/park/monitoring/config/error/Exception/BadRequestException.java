package com.park.monitoring.config.error.Exception;

import com.park.monitoring.config.error.ErrorCode;

public class BadRequestException extends IllegalArgumentException{
    ErrorCode errorCode;
    public BadRequestException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getMessage(){
        return errorCode.getMessage();
    }
}
