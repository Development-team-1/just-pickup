package com.justpickup.orderservice.domain.order.dto;

import com.justpickup.orderservice.domain.order.entity.Order;
import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.justpickup.orderservice.domain.orderItem.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter @Builder
public class OrderMainDto {
    private List<_Order> orders;
    private boolean hasNext;

    // == constructor == //
    public static OrderMainDto of(List<Order> orders, boolean hasNext) {
        return OrderMainDto.builder()
                .orders(orders.stream().map(_Order::of).collect(toList()))
                .hasNext(hasNext)
                .build();
    }

    // == inner class == //
    @Getter @Builder
    public static class _Order {
        private Long id;
        private Long userId;
        private OrderStatus orderStatus;
        private LocalDateTime orderTime;
        private List<_OrderItem> orderItems;

        private String userName;
        private String storeName;

        public static _Order of(Order order) {
            List<_OrderItem> orderItems = order.getOrderItems()
                    .stream()
                    .map(_OrderItem::of)
                    .collect(toList());

            return _Order.builder()
                    .id(order.getId())
                    .userId(order.getUserId())
                    .orderStatus(order.getOrderStatus())
                    .orderTime(order.getOrderTime())
                    .orderItems(orderItems)
                    .build();
        }

        public void changeUserName(String userName) {
            this.userName = userName;
        }
    }

    @Getter @Builder
    public static class _OrderItem {
        private Long id;
        private Long itemId;
        private String itemName;

        public static _OrderItem of(OrderItem orderItem) {
            return _OrderItem.builder()
                    .id(orderItem.getId())
                    .itemId(orderItem.getItemId())
                    .build();
        }

        public void changeItemName(String itemName) {
            this.itemName = itemName;
        }
    }
}
