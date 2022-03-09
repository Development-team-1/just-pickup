package com.justpickup.notificationservice.domain.notification.service;

import com.justpickup.notificationservice.domain.notification.dto.FindNotificationDto;

import java.util.List;

public interface NotificationService {
    List<FindNotificationDto> findNotificationByUserId(Long id);
}
