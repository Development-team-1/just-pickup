package com.justpickup.orderservice.domain.orderItem.repository;

import com.justpickup.orderservice.domain.orderItem.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
