package com.justpickup.orderservice.global.client.exception;

import com.justpickup.orderservice.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class FeignClientException extends CustomException {

    public FeignClientException(HttpStatus status, String message) {
        super(status, message);
    }
}
