package com.justpickup.orderservice.domain.order.service;

import com.justpickup.orderservice.domain.order.dto.*;
import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.justpickup.orderservice.domain.orderItem.dto.OrderItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

public interface OrderService {
    OrderMainDto findOrderMain(OrderSearchCondition condition, Long storeId);
    Page<OrderDto> findPrevOrderMain(PrevOrderSearch search, Pageable pageable, Long storeId);
    SliceImpl<OrderDto> findOrderHistory(Pageable pageable, Long userId);
    void addItemToBasket(OrderItemDto orderItemDto,Long storeId, Long userId);
    FetchOrderDto fetchOrder(Long userId);
    void saveOrder(Long userId);

    void modifyOrder(Long userId, OrderStatus orderStatus);
}
