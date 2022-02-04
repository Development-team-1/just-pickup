package com.justpickup.orderservice.domain.order.repository;

import com.justpickup.orderservice.domain.order.entity.Order;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.justpickup.orderservice.domain.order.entity.QOrder.order;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<Order> findOrderMainBetweenOrderDate(LocalDate orderDate) {
        LocalDateTime start = orderDate.atStartOfDay();
        LocalDateTime end = LocalDateTime.of(orderDate, LocalTime.of(23, 59, 59));

        return queryFactory
                .selectFrom(order)
                .join(order.orderItems).fetchJoin()
                .join(order.transaction).fetchJoin()
                .where(
                        order.orderTime.between(start, end)
                )
                .distinct()
                .fetch();
    }
}
