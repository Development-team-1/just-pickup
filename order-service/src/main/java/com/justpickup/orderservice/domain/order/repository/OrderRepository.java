package com.justpickup.orderservice.domain.order.repository;

import com.justpickup.orderservice.domain.order.entity.Order;
import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Long countByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);
    Optional<Order> findByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);

}
