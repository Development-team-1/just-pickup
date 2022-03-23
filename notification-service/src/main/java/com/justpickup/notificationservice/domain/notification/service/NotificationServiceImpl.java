package com.justpickup.notificationservice.domain.notification.service;

import com.justpickup.notificationservice.domain.notification.dto.FindNotificationDto;
import com.justpickup.notificationservice.domain.notification.dto.UpdateNotificationDto;
import com.justpickup.notificationservice.domain.notification.entity.Notification;
import com.justpickup.notificationservice.domain.notification.exception.NotExistNotification;
import com.justpickup.notificationservice.domain.notification.repository.NotificationRepository;
import com.justpickup.notificationservice.global.client.store.GetStoreResponse;
import com.justpickup.notificationservice.global.client.store.StoreClient;
import com.justpickup.notificationservice.global.dto.Yn;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final StoreClient storeClient;

    @Override
    public List<FindNotificationDto> findNotificationByUserId(Long userId) {
        Order readYnAsc = Order.asc("readYn");
        Order createdAtDesc = Order.desc("createdAt");

        Sort sort = Sort.by(List.of(readYnAsc, createdAtDesc));
        return notificationRepository.findByUserId(userId, sort)
                .stream()
                .map(FindNotificationDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateNotification(UpdateNotificationDto dto) {
        Long id = dto.getId();
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotExistNotification(id + "는 없는 알림 고유번호입니다."));

        Yn readYn = dto.isRead() ? Yn.Y : Yn.N;
        notification.modifyReadYn(readYn);
    }

    @Override
    public Long findNotificationCounts(Long userId, Yn readYn) {
        return notificationRepository.countByUserIdAndReadYn(userId, readYn);
    }

    @Transactional
    @Override
    public void insertOrderPlaced(Long userId, Long storeId) {
        GetStoreResponse storeResponse = storeClient.getStore(String.valueOf(storeId)).getData();

        String title = "주문이 신청되었어요.";
        String storeName = "[" + storeResponse.getName() + "] ";
        String message = storeName + "매장의 주문이 신청되었습니다.";
        Notification notification = Notification.of(userId, message, title);
        notificationRepository.save(notification);
    }

    @Transactional
    @Override
    public void insertOrderAccepted(Long userId, Long storeId) {
        GetStoreResponse storeInfo = storeClient.getStore(String.valueOf(storeId)).getData();

        String title = "주문이 수락되었어요.";
        String storeName = "[" + storeInfo.getName() + "] ";
        String message = storeName + "매장의 주문이 수락되었습니다.";
        Notification notification = Notification.of(userId, message, title);
        notificationRepository.save(notification);
    }

}
