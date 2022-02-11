package com.justpickup.orderservice.domain.order.repository;

import com.justpickup.orderservice.domain.order.dto.OrderSearchCondition;
import com.justpickup.orderservice.domain.order.dto.PrevOrderSearch;
import com.justpickup.orderservice.domain.order.entity.Order;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.justpickup.orderservice.domain.order.entity.QOrder.order;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /*
    SELECT *
    FROM order
    WHERE 조건문
    AND id < 마지막 조회 ID
    ORDER BY id desc
    LIMIT 페이지 사이즈
     */
    public List<Order> findOrderMain(OrderSearchCondition condition, Long storeId) {
        LocalDateTime start = condition.getOrderStartTime();
        LocalDateTime end = condition.getOrderEndTime();

        return queryFactory
                .selectFrom(order)
                .join(order.transaction).fetchJoin()
                .where(
                    order.orderTime.between(start, end),
                    order.storeId.eq(storeId),
                    orderIdLt(condition.getLastOrderId())
                )
                .orderBy(order.orderTime.desc())
                .limit(6)
                .distinct()
                .fetch();
    }

    private BooleanExpression orderIdLt(Long lastOrderId) {
        return lastOrderId != null ? order.id.lt(lastOrderId) : null;
    }

    public Page<Order> findPrevOrderMain(PrevOrderSearch search, Pageable pageable, Long storeId) {
        // 카운트 가져오기
        Long count = queryFactory
                .select(order.countDistinct())
                .from(order)
                .innerJoin(order.transaction)
                .where(
                        order.orderTime.between(search.getStartDateTime(), search.getEndDateTime()),
                        order.storeId.eq(storeId)
                )
                .fetchOne();

        // 데이터 가져오기
        List<Order> orders = queryFactory
                .selectFrom(order)
                .join(order.transaction).fetchJoin()
                .where(
                        order.orderTime.between(search.getStartDateTime(), search.getEndDateTime()),
                        order.storeId.eq(storeId)
                )
                .orderBy(order.orderTime.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .distinct()
                .fetch();

        return PageableExecutionUtils.getPage(orders, pageable, () -> count);
    }
}
