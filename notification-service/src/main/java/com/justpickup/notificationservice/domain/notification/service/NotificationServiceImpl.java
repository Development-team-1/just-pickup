package com.justpickup.notificationservice.domain.notification.service;

import com.justpickup.notificationservice.domain.notification.dto.FindNotificationDto;
import com.justpickup.notificationservice.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public List<FindNotificationDto> findNotificationByUserId(Long userId) {
        return notificationRepository.findByUserId(userId)
                .stream()
                .map(FindNotificationDto::new)
                .collect(Collectors.toList());
    }
}
