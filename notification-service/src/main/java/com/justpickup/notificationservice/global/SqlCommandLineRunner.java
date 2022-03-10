package com.justpickup.notificationservice.global;

import com.justpickup.notificationservice.domain.notification.entity.Notification;
import com.justpickup.notificationservice.domain.notification.repository.NotificationRepository;
import com.justpickup.notificationservice.global.dto.Yn;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SqlCommandLineRunner implements CommandLineRunner {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        for (long userId = 1; userId < 10; userId++) {
            List<Notification> notifications = new ArrayList<>();
            for (int notification = 1; notification <= 5; notification++) {
                Notification of = Notification.of(userId, notification + "번 매장의 주문이 수락되었습니다.", "주문이 수락되었어요");
                notifications.add(of);
            }
            for (int notification = 6; notification <= 10; notification++) {
                Notification of = Notification.of(userId, notification + "번 매장의 주문이 수락되었습니다.", "주문이 수락되었어요");
                of.modifyReadYn(Yn.Y);
                notifications.add(of);
            }
            notificationRepository.saveAll(notifications);
        }
    }
}
