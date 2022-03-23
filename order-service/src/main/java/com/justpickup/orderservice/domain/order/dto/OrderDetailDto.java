package com.justpickup.orderservice.domain.order.dto;

import com.justpickup.orderservice.domain.order.entity.Order;
import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.justpickup.orderservice.domain.orderItem.entity.OrderItem;
import com.justpickup.orderservice.domain.orderItemOption.entity.OrderItemOption;
import com.justpickup.orderservice.global.client.store.OptionType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetailDto {

    private Long id;
    private LocalDateTime orderTime;
    private Long orderPrice;
    private OrderStatus orderStatus;
    private String storeName;
    private OrderDetailUser user;
    private List<OrderDetailItem> orderItems = new ArrayList<>();

    @Builder
    public OrderDetailDto(Long id, LocalDateTime orderTime, Long orderPrice, OrderStatus orderStatus,
                          String storeName, OrderDetailUser user, List<OrderDetailItem> orderItems) {
        this.id = id;
        this.orderTime = orderTime;
        this.orderPrice = orderPrice;
        this.orderStatus = orderStatus;
        this.storeName = storeName;
        this.user = user;
        this.orderItems = orderItems;
    }

    public static OrderDetailDto of(Order order, String storeName,
                                    List<OrderDetailItem> orderItems, OrderDetailUser orderDetailUser) {
        OrderDetailDto orderDetailDto = new OrderDetailDto();
        orderDetailDto.id = order.getId();
        orderDetailDto.orderTime = order.getOrderTime();
        orderDetailDto.orderPrice = order.getOrderPrice();
        orderDetailDto.orderStatus = order.getOrderStatus();

        orderDetailDto.storeName = storeName;

        orderDetailDto.user = orderDetailUser;
        orderDetailDto.orderItems = orderItems;
        return orderDetailDto;
    }

    @Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class OrderDetailUser {
        private Long id;
        private String name;
        private String phoneNumber;

        @Builder
        public OrderDetailUser(Long id, String name, String phoneNumber) {
            this.id = id;
            this.name = name;
            this.phoneNumber = phoneNumber;
        }
    }

    @Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class OrderDetailItem {
        private Long id;
        private Long itemId;
        private long totalPrice;
        private long count;
        private String name;
        private List<OrderDetailItemOption> options = new ArrayList<>();

        @Builder
        public OrderDetailItem(Long id, Long itemId, long totalPrice, long count, String name, List<OrderDetailItemOption> options) {
            this.id = id;
            this.itemId = itemId;
            this.totalPrice = totalPrice;
            this.count = count;
            this.name = name;
            this.options = options;
        }

        public static OrderDetailItem of(OrderItem orderItem, String name, List<OrderDetailItemOption> orderDetailItemOption) {
            OrderDetailItem orderDetailItem = new OrderDetailItem();
            orderDetailItem.id = orderItem.getId();
            orderDetailItem.itemId = orderItem.getItemId();
            orderDetailItem.totalPrice = orderItem.getTotalPrice();
            orderDetailItem.count = orderItem.getCount();
            orderDetailItem.name = name;
            orderDetailItem.options = orderDetailItemOption;
            return orderDetailItem;
        }

    }

    @Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class OrderDetailItemOption {
        private Long id;
        private Long itemOptionId;
        private String name;
        private OptionType optionType;

        @Builder
        public OrderDetailItemOption(Long id, Long itemOptionId, String name, OptionType optionType) {
            this.id = id;
            this.itemOptionId = itemOptionId;
            this.name = name;
            this.optionType = optionType;
        }

        public static OrderDetailItemOption of(OrderItemOption orderItemOption, String name, OptionType optionType) {
            OrderDetailItemOption orderDetailItemOption = new OrderDetailItemOption();
            orderDetailItemOption.id = orderItemOption.getId();
            orderDetailItemOption.itemOptionId = orderItemOption.getItemOptionId();
            orderDetailItemOption.name = name;
            orderDetailItemOption.optionType = optionType;
            return orderDetailItemOption;
        }
    }
}
