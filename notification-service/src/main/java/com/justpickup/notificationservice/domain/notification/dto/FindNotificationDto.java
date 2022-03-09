package com.justpickup.notificationservice.domain.notification.dto;

import com.justpickup.notificationservice.domain.notification.entity.Notification;
import com.justpickup.notificationservice.global.dto.Yn;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(staticName = "of")
public class FindNotificationDto {
    private Long id;
    private Long userId;
    private String message;
    private String title;
    private Yn readYn;
    private LocalDateTime createdAt;

    public FindNotificationDto(Notification entity) {
        this.id = entity.getId();
        this.userId = entity.getUserId();
        this.message = entity.getMessage();
        this.title = entity.getTitle();
        this.readYn = entity.getReadYn();
        this.createdAt = entity.getCreatedAt();
    }
}
