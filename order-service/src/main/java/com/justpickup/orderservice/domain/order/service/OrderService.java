package com.justpickup.orderservice.domain.order.service;

import com.justpickup.orderservice.domain.order.dto.OrderDto;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    List<OrderDto> findOrderMain(LocalDate localDate);
}
