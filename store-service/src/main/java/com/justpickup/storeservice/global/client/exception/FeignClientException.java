package com.justpickup.storeservice.global.client.exception;


import com.justpickup.storeservice.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class FeignClientException extends CustomException {

    public FeignClientException(HttpStatus status, String message) {
        super(status, message);
    }
}
