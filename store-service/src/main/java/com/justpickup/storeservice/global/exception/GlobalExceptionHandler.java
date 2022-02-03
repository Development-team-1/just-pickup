package com.justpickup.storeservice.global.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorBody> customExceptionHandler(CustomException ce) {
        ErrorEnum errorEnum = ce.getErrorEnum();

        log.warn("##################################################");
        log.warn("## CustomException = {}", errorEnum);
        log.warn("##################################################");

        HttpStatus errorHttpStatus = errorEnum.getHttpStatus();

        return ResponseEntity.status(errorHttpStatus)
                .body(new ErrorBody(errorEnum.getMessage(), errorHttpStatus.getReasonPhrase()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorBody> bindExceptionHandler(BindException exception) {
        return getValidationErrorBody(exception);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorBody> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        return getValidationErrorBody(exception);
    }

    private ResponseEntity<ErrorBody> getValidationErrorBody(BindException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        StringBuilder builder = new StringBuilder();
        bindingResult.getFieldErrors()
                .forEach(fieldError -> {
                    builder.append("[");
                    builder.append(fieldError.getField());
                    builder.append("](은)는 ");
                    builder.append(fieldError.getDefaultMessage());
                    builder.append(" 입력된 값: [");
                    builder.append(fieldError.getRejectedValue());
                    builder.append("]");
                });

        ErrorBody errorBody = new ErrorBody(builder.toString(), HttpStatus.BAD_REQUEST.getReasonPhrase());

        log.warn("##################################################");
        log.warn("## getValidationErrorBody = {}", errorBody);
        log.warn("##################################################");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorBody);
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    static class ErrorBody {
        private String message;
        private String httpStatus;
    }

}
