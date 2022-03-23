package com.justpickup.orderservice.domain.order.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.justpickup.orderservice.domain.order.entity.Order;
import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.justpickup.orderservice.domain.orderItem.dto.OrderItemDto;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderSender {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void orderPlaced(Order order) throws Exception{
        objectMapper.registerModule(new JavaTimeModule());

        KafkaSendOrderDto kafkaSendOrderDto = KafkaSendOrderDto.createPrimitiveField(order);
        String jsonInString = objectMapper.writeValueAsString(kafkaSendOrderDto);
        kafkaTemplate.send("orderPlaced", jsonInString);
        log.info("kafka Producer sent data from the Order microservice: "+ kafkaSendOrderDto);
    }

    public void orderAccepted(Order order) throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        KafkaSendOrderDto kafkaSendOrderDto = KafkaSendOrderDto.createPrimitiveField(order);
        String json = objectMapper.writeValueAsString(kafkaSendOrderDto);
        kafkaTemplate.send("orderAccepted", json);
        log.info("[OrderSender] orderAccepted = {}", json);
    }

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class KafkaSendOrderDto {
        private Long id;

        private Long userId;

        private String userName;

        private Long userCouponId;

        private Long orderPrice;

        private Long storeId;

        private LocalDateTime orderTime;

        private Long usedPoint;

        private OrderStatus orderStatus;

        private List<OrderItemDto> orderItemDtoList;

        // == 생성 메소드 == //
        public static KafkaSendOrderDto createPrimitiveField(Order order) {
            return KafkaSendOrderDto.builder()
                    .id(order.getId())
                    .userId(order.getUserId())
                    .userCouponId(order.getUserCouponId())
                    .orderPrice(order.getOrderPrice())
                    .orderTime(order.getOrderTime())
                    .storeId(order.getStoreId())
                    .usedPoint(order.getUsedPoint())
                    .orderStatus(order.getOrderStatus())
                    .build();
        }
    }
}
