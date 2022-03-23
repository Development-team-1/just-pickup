package com.justpickup.orderservice.domain.orderItem.repository;

import com.justpickup.orderservice.domain.orderItem.entity.OrderItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.justpickup.orderservice.domain.orderItem.entity.QOrderItem.orderItem;

@Repository
@RequiredArgsConstructor
public class OrderItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<OrderItem> getOrderItemsWithOptions(Long orderId) {
        return queryFactory.selectFrom(orderItem)
                .leftJoin(orderItem.orderItemOptions).fetchJoin()
                .where(
                        orderItem.order.id.eq(orderId)
                )
                .distinct()
                .fetch();
    }
}
