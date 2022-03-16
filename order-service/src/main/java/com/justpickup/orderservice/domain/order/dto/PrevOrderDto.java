package com.justpickup.orderservice.domain.order.dto;

import com.justpickup.orderservice.domain.order.entity.Order;
import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.justpickup.orderservice.domain.orderItem.entity.OrderItem;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PrevOrderDto {
    private Long id;
    private OrderStatus orderStatus;
    private LocalDateTime orderTime;
    private long orderPrice;
    private Long userId;
    private String userName;

    private List<_PrevOrderItem> orderItems = new ArrayList<>();

    @Builder
    public PrevOrderDto(Long id, OrderStatus orderStatus, LocalDateTime orderTime, long orderPrice, Long userId, String userName, List<_PrevOrderItem> orderItems) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderTime = orderTime;
        this.orderPrice = orderPrice;
        this.userId = userId;
        this.userName = userName;
        this.orderItems = orderItems;
    }

    @Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class _PrevOrderItem {
        private Long id;
        private Long itemId;
        private String name;

        @Builder
        public _PrevOrderItem(Long id, Long itemId, String name) {
            this.id = id;
            this.itemId = itemId;
            this.name = name;
        }

        // 생성 메소드
        static _PrevOrderItem of(OrderItem orderItem) {
            _PrevOrderItem prevOrderItem = new _PrevOrderItem();
            prevOrderItem.id = orderItem.getId();
            prevOrderItem.itemId = orderItem.getItemId();
            return prevOrderItem;
        }

        public void changeName(String name) {
            this.name = name;
        }
    }

    // 생성 메소드
    public static PrevOrderDto of(Order order) {
        PrevOrderDto prevOrder = new PrevOrderDto();
        prevOrder.id = order.getId();
        prevOrder.orderStatus = order.getOrderStatus();
        prevOrder.orderTime = order.getOrderTime();
        prevOrder.orderPrice = order.getOrderPrice();
        prevOrder.userId = order.getUserId();

        prevOrder.orderItems = order.getOrderItems()
                .stream()
                .map(_PrevOrderItem::of)
                .collect(toList());

        return prevOrder;
    }

    public void changeUserName(String userName) {
        this.userName = userName;
    }

}