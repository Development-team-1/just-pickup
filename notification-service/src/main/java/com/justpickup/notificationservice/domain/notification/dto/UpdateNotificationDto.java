package com.justpickup.notificationservice.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class UpdateNotificationDto {
    private Long id;
    private boolean read;
}
