package com.justpickup.notificationservice.domain.notification.exception;

import com.justpickup.notificationservice.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class NotExistNotification extends CustomException {

    public NotExistNotification(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
