package com.justpickup.userservice.domain.jwt.exception;

import com.justpickup.userservice.global.dto.Result;
import lombok.Getter;

@Getter
public class RefreshTokenNotValidException extends RuntimeException {

    private Result result;

    public RefreshTokenNotValidException(String message) {
        this.result = Result.createErrorResult(message);
    }
}
