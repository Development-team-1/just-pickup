package com.justpickup.orderservice.domain.order.entity;

import com.justpickup.orderservice.domain.order.dto.OrderDto;
import com.justpickup.orderservice.domain.orderItem.dto.OrderItemDto;
import com.justpickup.orderservice.domain.orderItem.entity.OrderItem;
import com.justpickup.orderservice.domain.transaction.entity.Transaction;
import com.justpickup.orderservice.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    private Long userId;

    private Long userCouponId;

    private Long orderPrice;

    private LocalDateTime orderTime;

    private Long usedPoint;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
    private Transaction transaction;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;

    // == 변환 메소드 == //
    public OrderDto toOrderDto() {
        List<OrderItemDto> orderItemDtoList = orderItems.stream()
                .map(OrderItem::toOrderItemDto)
                .collect(Collectors.toList());

        return OrderDto.builder()
                .id(id)
                .userId(userId)
                .userCouponId(userCouponId)
                .orderPrice(orderPrice)
                .orderTime(orderTime)
                .usedPoint(usedPoint)
                .orderStatus(orderStatus)
                .orderItemDtoList(orderItemDtoList)
                .build();
    }

}
