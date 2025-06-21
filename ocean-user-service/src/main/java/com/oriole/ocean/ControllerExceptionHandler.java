package com.oriole.ocean;

import com.oriole.ocean.common.vo.BusinessException;
import com.oriole.ocean.common.vo.MsgEntity;
import com.oriole.ocean.common.vo.OceanExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@ResponseBody
public class ControllerExceptionHandler extends OceanExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    @Override
    public MsgEntity<OceanExceptionHandler.ErrorMsg> businessExceptionHandler(BusinessException ex) {
        return super.businessExceptionHandler(ex);
    }

    /**
     * Handle validation errors from @Valid annotation on request body
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MsgEntity<ErrorMsg> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        
        log.warn("Validation error: {}", errorMessage);
        ErrorMsg errorMsg = new ErrorMsg(
                "VALIDATION_ERROR",
                "handleValidationException",
                "ControllerExceptionHandler",
                "数据验证失败: " + errorMessage);
        return new MsgEntity<>("VALIDATION_ERROR", "-8", errorMsg);
    }

    /**
     * Handle validation errors from @Validated annotation on method parameters
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MsgEntity<ErrorMsg> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        
        log.warn("Constraint violation: {}", errorMessage);
        ErrorMsg errorMsg = new ErrorMsg(
                "CONSTRAINT_VIOLATION",
                "handleConstraintViolationException",
                "ControllerExceptionHandler",
                "参数验证失败: " + errorMessage);
        return new MsgEntity<>("VALIDATION_ERROR", "-9", errorMsg);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
    @Override
    public MsgEntity<ErrorMsg> systemUnexpectedExceptionHandler(Exception ex) {
        return super.systemUnexpectedExceptionHandler(ex);
    }
}
