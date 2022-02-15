package com.justpickup.userservice.domain.user.exception;

import com.justpickup.userservice.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class NotExistUserException extends CustomException {

    public NotExistUserException(String message) {
        super(HttpStatus.CONFLICT, message);
    }

}
