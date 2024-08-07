package com.park.monitoring.config.error.Exception;

import com.park.monitoring.config.error.ErrorCode;

public class NotFoundException extends BaseException{
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public NotFoundException(){
        super(ErrorCode.NOT_FOUND);
    }
}
