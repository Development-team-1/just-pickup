package com.justpickup.userservice.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private ErrorEnum errorEnum;

    protected CustomException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.errorEnum = errorEnum;
    }
}
