package com.park.monitoring.config.error;

import com.park.monitoring.config.error.Exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(NotFoundException.class)
    protected String handleNotFoundException(Model model, NotFoundException e) {
        errorLog(e);
        model.addAttribute("err", e.getErrorCode());
        return "errPage"; // 에러 페이지 이름
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected String handleMethodArgumentTypeMismatchException(Model model, MethodArgumentTypeMismatchException e) {
        errorLog(e);
        model.addAttribute("err", ErrorCode.INVALID_INPUT_VALUE); // 예를 들어, INVALID_INPUT_VALUE를 사용
        return "errPage"; // 에러 페이지 이름
    }

    @ExceptionHandler(BadRequestException.class)
    protected String handleBadRequestException(Model model, BadRequestException e) {
        errorLog(e);
        model.addAttribute("err", e.getErrorCode());
        return "errPage"; // 에러 페이지 이름
    }

    @ExceptionHandler(DataIntegrityException.class)
    protected String handleDataIntegrityException(Model model, DataIntegrityException e) {
        errorLog(e);
        model.addAttribute("err", e.getErrorCode());
        return "errPage"; // 에러 페이지 이름
    }

    @ExceptionHandler(NoContentException.class)
    protected String handleNoContentException(Model model, NoContentException e) {
        errorLog(e);
        model.addAttribute("err", e.getErrorCode());
        return "errPage"; // 에러 페이지 이름
    }

    @ExceptionHandler(BaseException.class)
    protected String handleBaseException(Model model, BaseException e) {
        errorLog(e);
        model.addAttribute("err", e.getErrorCode());
        return "errPage"; // 에러 페이지 이름
    }

    @ExceptionHandler(Exception.class)
    protected String handleGenericException(Model model, Exception e) {
        errorLog(e);
        model.addAttribute("err", ErrorCode.UNEXPECTED_ERROR); // 예를 들어, UNEXPECTED_ERROR를 사용
        return "errPage"; // 에러 페이지 이름
    }

    private void errorLog(Exception e) {
        log.error("Exception [Err_Location] : {}", e.getStackTrace()[0]);
    }
}