package com.justpickup.orderservice.domain.order.repository;

import com.justpickup.orderservice.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
