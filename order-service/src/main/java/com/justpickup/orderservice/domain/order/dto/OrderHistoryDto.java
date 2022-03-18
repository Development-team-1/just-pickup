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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderHistoryDto {
    private Long id;
    private LocalDateTime orderTime;
    private long price;
    private OrderStatus orderStatus;
    private Long storeId;
    private String storeName;
    private List<_OrderHistoryItem> orderItems = new ArrayList<>();

    @Builder
    public OrderHistoryDto(Long id, LocalDateTime orderTime, long price, OrderStatus orderStatus, Long storeId, String storeName, List<_OrderHistoryItem> orderItems) {
        this.id = id;
        this.orderTime = orderTime;
        this.price = price;
        this.orderStatus = orderStatus;
        this.storeId = storeId;
        this.storeName = storeName;
        this.orderItems = orderItems;
    }

    @Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class _OrderHistoryItem {
        private Long id;
        private Long itemId;
        private String itemName;

        @Builder
        public _OrderHistoryItem(Long id, Long itemId, String itemName) {
            this.id = id;
            this.itemId = itemId;
            this.itemName = itemName;
        }

        public static _OrderHistoryItem of(OrderItem orderItem) {
            _OrderHistoryItem orderHistoryItem = new _OrderHistoryItem();
            orderHistoryItem.id = orderItem.getId();
            orderHistoryItem.itemId = orderItem.getItemId();
            return orderHistoryItem;
        }

        public void changeItemName(String itemName) {
            this.itemName = itemName;
        }
    }

    public static OrderHistoryDto of(Order order) {
        OrderHistoryDto orderHistoryDto = new OrderHistoryDto();
        orderHistoryDto.id = order.getId();
        orderHistoryDto.orderTime = order.getOrderTime();
        orderHistoryDto.price = order.getOrderPrice();
        orderHistoryDto.orderStatus = order.getOrderStatus();
        orderHistoryDto.storeId = order.getStoreId();

        orderHistoryDto.orderItems = order.getOrderItems().stream()
                .map(_OrderHistoryItem::of)
                .collect(Collectors.toList());
        return orderHistoryDto;
    }

    public void changeStoreName(String storeName) {
        this.storeName = storeName;
    }
}
