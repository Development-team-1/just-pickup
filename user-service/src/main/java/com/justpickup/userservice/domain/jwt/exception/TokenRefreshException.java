package com.justpickup.userservice.domain.jwt.exception;

import com.justpickup.userservice.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class TokenRefreshException extends CustomException {

    public TokenRefreshException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
