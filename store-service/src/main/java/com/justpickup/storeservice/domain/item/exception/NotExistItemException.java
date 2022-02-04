package com.justpickup.storeservice.domain.item.exception;

import com.justpickup.storeservice.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class NotExistItemException extends CustomException {

    public NotExistItemException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
