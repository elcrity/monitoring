package com.park.monitoring.config.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "E1", "올바르지 않은 입력값입니다."),
    INVALID_ACCESS(HttpStatus.BAD_REQUEST, "E1-1", "비정상적인 접근입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "E2", "잘못된 HTTP 메서드를 호출했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E3", "서버 에러가 발생했습니다."),
    DUPLICATED_ENTITY(HttpStatus.CONFLICT, "E4", "중복된 엔티티가 존재합니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "E5", "존재하지 않는 엔티티입니다."),
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E6", "예상치 못한 에러가 발생했습니다."),
    NO_CONTENT(HttpStatus.NO_CONTENT, "s1", "작업이 성공적으로 완료되었으나 반환할 데이터가 없습니다")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
    ErrorCode(HttpStatus status, String code, String message) {
        this.message = message;
        this.status = status;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
