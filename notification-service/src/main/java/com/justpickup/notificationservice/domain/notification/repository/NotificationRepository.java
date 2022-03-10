package com.justpickup.notificationservice.domain.notification.repository;

import com.justpickup.notificationservice.domain.notification.entity.Notification;
import com.justpickup.notificationservice.global.dto.Yn;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId, Sort sort);
    long countByUserIdAndReadYn(Long userId, Yn readYn);
}
