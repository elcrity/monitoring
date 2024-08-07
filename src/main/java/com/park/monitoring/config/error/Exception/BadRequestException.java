package com.park.monitoring.config.error.Exception;

import com.park.monitoring.config.error.ErrorCode;

public class BadRequestException extends BaseException{

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }
    public BadRequestException() {
        super(ErrorCode.INVALID_INPUT_VALUE);
    }
}
