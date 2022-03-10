package com.justpickup.orderservice.domain.orderItemOption.repository;


import com.justpickup.orderservice.domain.orderItemOption.entity.OrderItemOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemOptionRepository extends JpaRepository<OrderItemOption , Long> {
}
