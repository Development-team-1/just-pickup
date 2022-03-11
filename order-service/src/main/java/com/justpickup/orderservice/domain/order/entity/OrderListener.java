package com.justpickup.orderservice.domain.order.entity;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.PostUpdate;

@Slf4j
public class OrderListener {

    @PostUpdate
    private void postUpdate(Order order) {
        OrderStatus orderStatus = order.getOrderStatus();
        if (orderStatus == OrderStatus.ORDER) {
            // TODO: 2022/03/10 Kafka 알림 전송
            log.info("[OrderListener] {}", OrderStatus.ORDER.name());
        } else if (orderStatus == OrderStatus.PLACED) {
            log.info("[OrderListener] {}", OrderStatus.PLACED.name());
        }
    }
}
