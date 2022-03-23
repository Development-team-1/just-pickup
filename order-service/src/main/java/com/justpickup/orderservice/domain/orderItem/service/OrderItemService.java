package com.justpickup.orderservice.domain.orderItem.service;

import com.justpickup.orderservice.domain.order.entity.Order;
import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.justpickup.orderservice.domain.order.exception.OrderException;
import com.justpickup.orderservice.domain.order.repository.OrderRepository;
import com.justpickup.orderservice.domain.order.repository.OrderRepositoryCustom;
import com.justpickup.orderservice.domain.orderItem.entity.OrderItem;
import com.justpickup.orderservice.domain.orderItem.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final OrderRepositoryCustom orderRepositoryCustom;

    @Transactional
    public void deleteOrderItem(Long deleteOrderItemId, Long userId){


        Order order = orderRepositoryCustom.fetchOrderBasket(userId)
                .orElseThrow(() -> new OrderException("존재하지 않는 장바구니 아이템입니다."));

        OrderItem orderItem = orderItemRepository.findById(deleteOrderItemId)
                .orElseThrow(() -> new OrderException("존재하지 않는 장바구니 아이템입니다."));

        order.deleteOrderItem(orderItem);

        if(order.getOrderItems().size() ==0)
            orderRepository.delete(order);
    }
}
