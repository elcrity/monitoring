package com.park.monitoring.config.error.Exception;

import com.park.monitoring.config.error.ErrorCode;
import org.springframework.web.HttpRequestMethodNotSupportedException;

public class NotSupportedMethodException extends HttpRequestMethodNotSupportedException {
    ErrorCode errorCode;
    public NotSupportedMethodException(ErrorCode errorCode) {
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
