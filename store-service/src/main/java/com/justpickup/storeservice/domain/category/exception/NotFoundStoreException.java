package com.justpickup.storeservice.domain.category.exception;

import com.justpickup.storeservice.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class NotFoundStoreException extends CustomException {

    public NotFoundStoreException(HttpStatus status, String message){
        super(status,message);
    }
}
