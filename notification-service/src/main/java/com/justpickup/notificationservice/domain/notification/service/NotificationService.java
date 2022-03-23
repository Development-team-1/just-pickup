package com.justpickup.notificationservice.domain.notification.service;

import com.justpickup.notificationservice.domain.notification.dto.FindNotificationDto;
import com.justpickup.notificationservice.domain.notification.dto.UpdateNotificationDto;
import com.justpickup.notificationservice.global.dto.Yn;

import java.util.List;

public interface NotificationService {
    List<FindNotificationDto> findNotificationByUserId(Long id);
    void updateNotification(UpdateNotificationDto dto);
    Long findNotificationCounts(Long userId, Yn readYn);
    void insertOrderPlaced(Long userId, Long storeId);
    void insertOrderAccepted(Long userId, Long storeId);
}
