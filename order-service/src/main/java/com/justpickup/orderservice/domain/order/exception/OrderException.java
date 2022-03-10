package com.justpickup.orderservice.domain.order.exception;

import com.justpickup.orderservice.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class OrderException extends CustomException {

    public OrderException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
