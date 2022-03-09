package com.justpickup.notificationservice.domain.notification.service;

import com.justpickup.notificationservice.domain.notification.dto.FindNotificationDto;
import com.justpickup.notificationservice.domain.notification.dto.UpdateNotificationDto;

import java.util.List;

public interface NotificationService {
    List<FindNotificationDto> findNotificationByUserId(Long id);
    void updateNotification(UpdateNotificationDto dto);
}
