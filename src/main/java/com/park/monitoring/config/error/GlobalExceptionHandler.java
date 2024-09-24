package com.park.monitoring.config.error;

import com.park.monitoring.config.error.Exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//ErrorResponse로 return하고 front에서 잡아서 errPage로 보내기
//view와 함께 내보내면, 특정 id, fragment로 반환하는 controller는
//해당되는 rag만 에러 뷰로 전환됨
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e){
        errorLog(e);
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        // 예외 메시지를 사용하여 BAD_REQUEST 상태와 함께 응답을 생성합니다.
        errorLog(e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(BadRequestException e) {
        errorLog(e);
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }
    @ExceptionHandler(DataIntegrityException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateDataException(DataIntegrityException e) {
//            throw new DataIntegrityException(ErrorCode.DUPLICATED_ENTITY);
        errorLog(e);
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<ErrorResponse> handleNoContentException(NoContentException e) {
        // 예외 메시지를 사용하여 INTERNAL_SERVER_ERROR 상태와 함께 응답을 생성합니다.
        errorLog(e);
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
        // 예외 메시지를 사용하여 INTERNAL_SERVER_ERROR 상태와 함께 응답을 생성합니다.
        errorLog(e);
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        errorLog(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    private void errorLog(Exception e){
        log.error("Exception [Err_Location] : {}", e.getStackTrace()[0]);
    }
}
//@ControllerAdvice
//public class GlobalExceptionHandler {
//    Logger log = LoggerFactory.getLogger(this.getClass());
//
//    @ExceptionHandler(NotFoundException.class)
//    protected String handleNotFoundException(Model model, NotFoundException e) {
//        errorLog(e);
//        model.addAttribute("err", e.getErrorCode());
//        return "errPage"; // 에러 페이지 이름
//    }
//
//    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    protected String handleMethodArgumentTypeMismatchException(Model model, MethodArgumentTypeMismatchException e) {
//        errorLog(e);
//        model.addAttribute("err", ErrorCode.INVALID_INPUT_VALUE); // 예를 들어, INVALID_INPUT_VALUE를 사용
//        return "errPage"; // 에러 페이지 이름
//    }
//
//    @ExceptionHandler(BadRequestException.class)
//    protected String handleBadRequestException(Model model, BadRequestException e) {
//        errorLog(e);
//        model.addAttribute("err", e.getErrorCode());
//        return "errPage"; // 에러 페이지 이름
//    }
//
//    @ExceptionHandler(DataIntegrityException.class)
//    protected String handleDataIntegrityException(Model model, DataIntegrityException e) {
//        errorLog(e);
//        model.addAttribute("err", e.getErrorCode());
//        return "errPage"; // 에러 페이지 이름
//    }
//
//    @ExceptionHandler(NoContentException.class)
//    protected String handleNoContentException(Model model, NoContentException e) {
//        errorLog(e);
//        model.addAttribute("err", e.getErrorCode());
//        return "errPage"; // 에러 페이지 이름
//    }
//
//    @ExceptionHandler(BaseException.class)
//    protected String handleBaseException(Model model, BaseException e) {
//        errorLog(e);
//        model.addAttribute("err", e.getErrorCode());
//        return "errPage"; // 에러 페이지 이름
//    }
//
//    @ExceptionHandler(Exception.class)
//    protected String handleGenericException(Model model, Exception e) {
//        errorLog(e);
//        model.addAttribute("err", ErrorCode.UNEXPECTED_ERROR); // 예를 들어, UNEXPECTED_ERROR를 사용
//        return "errPage"; // 에러 페이지 이름
//    }
//
//    private void errorLog(Exception e) {
//        log.error("Exception [Err_Location] : {}", e.getStackTrace()[0]);
//    }
//}