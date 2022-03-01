package com.justpickup.userservice.domain.user.exception;

import com.justpickup.userservice.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class DuplicateUserEmail extends CustomException {

    public DuplicateUserEmail(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
