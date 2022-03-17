package com.justpickup.notificationservice.domain.notification.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.justpickup.notificationservice.domain.notification.service.NotificationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @KafkaListener(topics = "orderApply")
    public void applyOrder(String kafkaMessage) throws JsonProcessingException {
        log.debug("## NotificationConsumer.applyOrder");
        log.debug("#### kafka Message = {}", kafkaMessage);

        KafkaSendOrderDto kafkaSendOrderDto = objectMapper.readValue(kafkaMessage, KafkaSendOrderDto.class);


    }

    @Data
    static class KafkaSendOrderDto {
        private Long id;
        private Long userId;
        private Long userCouponId;
        private Long storeId;
        private long orderPrice;
        private LocalDateTime orderTime;
        private long usedPoint;
        private OrderStatus orderStatus;
    }
}
