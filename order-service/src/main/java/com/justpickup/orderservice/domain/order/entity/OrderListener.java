package com.justpickup.orderservice.domain.order.entity;

import com.justpickup.orderservice.domain.order.exception.OrderException;
import com.justpickup.orderservice.domain.order.service.OrderSender;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PostUpdate;

@Slf4j
@NoArgsConstructor
public class OrderListener {

    @Autowired
    @Lazy
    private OrderSender orderSender;

    @PostUpdate
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void postUpdate(Order order) {
        OrderStatus orderStatus = order.getOrderStatus();
        if (orderStatus == OrderStatus.ORDER) {
            // TODO: 2022/03/10 Kafka 알림 전송
            log.info("[OrderListener] {}", OrderStatus.ORDER.name());

            try{
                orderSender.orderPlaced(OrderSender.KafkaSendOrderDto.createPrimitiveField(order));
            }catch (Exception ex){

                order.fail();

                throw new OrderException(ex.getMessage());
            }


        } else if (orderStatus == OrderStatus.PLACED) {
            log.info("[OrderListener] {}", OrderStatus.PLACED.name());
        }
    }
}
