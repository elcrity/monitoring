package com.park.monitoring.config.error;

import com.park.monitoring.config.error.Exception.BadRequestException;
import com.park.monitoring.config.error.Exception.BaseException;
import com.park.monitoring.config.error.Exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

//@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        // 예외 메시지를 사용하여 BAD_REQUEST 상태와 함께 응답을 생성합니다.
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
        // 예외 메시지를 사용하여 BAD_REQUEST 상태와 함께 응답을 생성합니다.
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

//    @ExceptionHandler(NoSuchElementException.class)
//    public ResponseEntity handleNoSuchElementException(NoSuchElementException e) {
//        System.out.println("cause : " + e.getCause());
//        Arrays.stream(e.getStackTrace())
//                .forEach(stackTraceElement ->
//                        System.out.println("--1 :  " + stackTraceElement.toString()));
//        // 예외 메시지를 사용하여 BAD_REQUEST 상태와 함께 응답을 생성합니다.
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//    }

//    @ExceptionHandler(NoSuchElementException.class)
//    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException e) {
//        // 예외 메시지를 사용하여 NOT_FOUND 상태와 함께 응답을 생성합니다.
//        final ErrorResponseDto errorResponse = new ErrorResponseDto.Builder()
////                .status(HttpStatus.NOT_FOUND.value())
//                .code("NOTFOUND")
////                .message(e.getMessage())
//                .build();
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
//    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        // 예외 메시지를 사용하여 INTERNAL_SERVER_ERROR 상태와 함께 응답을 생성합니다.
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

//        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e) {
//        log.error("HttpRequestMethodNotSupportedException", e);
//        return createErrorResponseEntity(ErrorCode.METHOD_NOT_ALLOWED);
//    }

//    @ExceptionHandler(NotFoundException.class)
//    protected ResponseEntity<ErrorResponse> handleNotFoundException(BaseException e) {
//        log.error("BadRequestException", e);
//        return createErrorResponseEntity(ErrorCode.NOT_FOUND);
//    }
//
//    @ExceptionHandler(BadRequestException.class)
//    protected ResponseEntity<ErrorResponse> handleBadRequestException(BaseException e) {
//        log.error("BadRequestException", e);
//        return createErrorResponseEntity(ErrorCode.INVALID_INPUT_VALUE);
//    }
//
//    @ExceptionHandler(NoSuchElementException.class)
//    protected ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
//        log.error("NoSuchElementException", e);
//        return createErrorResponseEntity(ErrorCode.NOT_FOUND);
//    }
//
//    @ExceptionHandler(BaseException.class)
//    protected ResponseEntity<ErrorResponse> handle(BaseException e) {
//        log.error("BaseException", e);
//        return createErrorResponseEntity(e.getErrorCode());
//    }
//
//    @ExceptionHandler(Exception.class)
//    protected ResponseEntity<ErrorResponse> handle(Exception e) {
//        e.printStackTrace();
//        log.error("Exception", e);
//        return createErrorResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
//    }

//    private ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode) {
//        return new ResponseEntity<>(
//                ErrorResponse.of(errorCode),
//                errorCode.getStatus());
//    }

}
