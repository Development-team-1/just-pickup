package com.justpickup.userservice.domain.jwt.exception;

import com.justpickup.userservice.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class AccessTokenNotValidException extends CustomException {

    public AccessTokenNotValidException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
