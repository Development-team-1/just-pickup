package com.justpickup.storeservice.domain.store.exception;

import com.justpickup.storeservice.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class NotExistStoreException extends CustomException {

    public NotExistStoreException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
