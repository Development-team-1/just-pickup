package com.justpickup.notificationservice.domain.notification.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.justpickup.notificationservice.domain.notification.service.NotificationService;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @Transactional
    @KafkaListener(topics = "orderPlaced")
    public void orderPlaced(String kafkaMessage) throws JsonProcessingException {
        log.debug("## NotificationConsumer.orderPlaced");
        log.debug("#### kafka Message = {}", kafkaMessage);

        KafkaSendOrderDto kafkaSendOrderDto = objectMapper.readValue(kafkaMessage, KafkaSendOrderDto.class);

        notificationService.insertOrderPlaced(kafkaSendOrderDto.getUserId(), kafkaSendOrderDto.getStoreId());
    }

    @Transactional
    @KafkaListener(topics = "orderAccepted")
    public void orderAccepted(String kafkaMessage) throws JsonProcessingException {
        log.debug("## NotificationConsumer.orderAccepted");
        log.debug("#### kafka Message = {}", kafkaMessage);

        KafkaSendOrderDto orderDto = objectMapper.readValue(kafkaMessage, KafkaSendOrderDto.class);

        notificationService.insertOrderAccepted(orderDto.userId, orderDto.storeId);
    }

    @Data @NoArgsConstructor
    static class KafkaSendOrderDto {
        private Long id;
        private Long userId;
        private Long userCouponId;
        private Long storeId;
        private long orderPrice;
        private LocalDateTime orderTime;
        private long usedPoint;
        private OrderStatus orderStatus;

        @Builder
        public KafkaSendOrderDto(Long id, Long userId, Long userCouponId, Long storeId,
                                 long orderPrice, LocalDateTime orderTime, long usedPoint, OrderStatus orderStatus) {
            this.id = id;
            this.userId = userId;
            this.userCouponId = userCouponId;
            this.storeId = storeId;
            this.orderPrice = orderPrice;
            this.orderTime = orderTime;
            this.usedPoint = usedPoint;
            this.orderStatus = orderStatus;
        }
    }
}
