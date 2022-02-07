package com.justpickup.orderservice.domain.order.service;

import com.justpickup.orderservice.domain.order.dto.OrderDto;
import com.justpickup.orderservice.domain.order.dto.OrderSearchCondition;

import java.util.List;

public interface OrderService {
    List<OrderDto> findOrderMain(OrderSearchCondition condition, Long storeId);
}
