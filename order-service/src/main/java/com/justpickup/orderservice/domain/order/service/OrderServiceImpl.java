package com.justpickup.orderservice.domain.order.service;

import com.justpickup.orderservice.domain.order.dto.OrderDto;
import com.justpickup.orderservice.domain.order.entity.Order;
import com.justpickup.orderservice.domain.order.repository.OrderRepository;
import com.justpickup.orderservice.domain.order.repository.OrderRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderRepositoryCustom orderRepositoryCustom;

    @Override
    public List<OrderDto> findOrderMain(LocalDate orderDate) {
        // 주문 가져오기
        List<OrderDto> orderDtoList = orderRepositoryCustom.findOrderMainBetweenOrderDate(orderDate)
                .stream()
                .map(Order::toOrderDto)
                .collect(Collectors.toList());

        // Feign Client 를 통한 주문 상품 가져오기

        return orderDtoList;
    }
}
