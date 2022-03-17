package com.justpickup.orderservice.domain.order.repository;

import com.justpickup.orderservice.domain.order.dto.OrderMainResult;
import com.justpickup.orderservice.domain.order.dto.OrderSearchCondition;
import com.justpickup.orderservice.domain.order.dto.PrevOrderSearch;
import com.justpickup.orderservice.domain.order.entity.Order;
import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.justpickup.orderservice.domain.order.entity.QOrder.order;
import static com.justpickup.orderservice.domain.orderItem.entity.QOrderItem.orderItem;
import static com.justpickup.orderservice.domain.orderItemOption.entity.QOrderItemOption.orderItemOption;
import static com.justpickup.orderservice.domain.transaction.entity.QTransaction.transaction;

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
    public OrderMainResult findOrderMain(OrderSearchCondition condition, Long storeId) {
        LocalDateTime start = condition.getOrderStartTime();
        LocalDateTime end = condition.getOrderEndTime();
        int pageSize = 6;

        List<Order> orders = queryFactory
                .selectFrom(order)
                .leftJoin(order.transaction)
                .where(
                        orderIdLt(condition.getLastOrderId()),
                        order.orderTime.between(start, end),
                        order.storeId.eq(storeId),
                        order.orderStatus.ne(OrderStatus.PENDING),
                        order.orderStatus.ne(OrderStatus.FAILED)
                )
                .orderBy(order.id.desc())
                .limit(pageSize + 1)
                .distinct()
                .fetch();

        boolean hasNext = false;
        if (orders.size() > pageSize) {
            orders.remove(pageSize);
            hasNext = true;
        }

        return OrderMainResult.of(orders, hasNext);
    }

    private BooleanExpression orderIdLt(Long lastOrderId) {
        return lastOrderId != null ? order.id.lt(lastOrderId) : null;
    }

    public Page<Order> findPrevOrderMain(PrevOrderSearch search, Pageable pageable, Long storeId) {
        // 카운트 가져오기
        Long count = queryFactory
                .select(order.countDistinct())
                .from(order)
                .leftJoin(order.transaction)
                .where(
                        order.orderTime.between(search.getStartDateTime(), search.getEndDateTime()),
                        order.storeId.eq(storeId)
                )
                .fetchOne();

        // 데이터 가져오기
        List<Order> orders = queryFactory
                .selectFrom(order)
                .leftJoin(order.transaction).fetchJoin()
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

    // Customer History
    public SliceImpl<Order> findOrderHistory(Pageable pageable, Long userId) {
        List<Order> contents = queryFactory
                .selectFrom(order)
                .leftJoin(order.transaction).fetchJoin()
                .where(
                        order.userId.eq(userId)
                )
                .orderBy(order.orderTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .distinct()
                .fetch();

        boolean hasNext = false;
        if (contents.size() > pageable.getPageSize()) {
            contents.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(contents, pageable, hasNext);
    }

    public Optional<Order> fetchOrder(Long userId){

        return Optional.ofNullable(queryFactory.selectFrom(order)
                .leftJoin(order.orderItems, orderItem).fetchJoin()
                .leftJoin(orderItem.orderItemOptions,orderItemOption)
                .leftJoin(order.transaction,transaction)
                    .where(
                            order.userId.eq(userId),
                            order.orderStatus.eq(OrderStatus.PENDING)
                    )
                        .distinct()
                            .fetchOne());

    }

}
