package com.park.monitoring.config.error;

import com.park.monitoring.config.error.Exception.BadRequestException;
import com.park.monitoring.config.error.Exception.BaseException;
import com.park.monitoring.config.error.Exception.DataIntegrityException;
import com.park.monitoring.config.error.Exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    Logger log = LoggerFactory.getLogger(this.getClass());


    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e){
        log.error("Exception [Err_Location] : {}", e.getStackTrace()[0]);
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        // 예외 메시지를 사용하여 BAD_REQUEST 상태와 함께 응답을 생성합니다.
        log.error("Exception [Err_Location] : {}", e.getStackTrace()[0]);
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(BadRequestException e) {
        log.error("Exception [Err_Location] : {}", e.getStackTrace()[0]);
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }
    @ExceptionHandler(DataIntegrityException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateDataException(DataIntegrityException e) {
//            throw new DataIntegrityException(ErrorCode.DUPLICATED_ENTITY);
        log.error("Exception [Err_Location] : {}", e.getStackTrace()[0]);
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(BaseException e) {
        // 예외 메시지를 사용하여 INTERNAL_SERVER_ERROR 상태와 함께 응답을 생성합니다.
        log.error("Exception [Err_Location] : {}", e.getStackTrace()[0]);
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        log.error("Exception [Err_Location] : {}", e.getStackTrace()[0]);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

}
