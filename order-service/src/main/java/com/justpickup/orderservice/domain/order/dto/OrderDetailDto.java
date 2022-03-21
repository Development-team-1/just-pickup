package com.justpickup.orderservice.domain.order.dto;

import com.justpickup.orderservice.domain.order.entity.Order;
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
    private OrderDetailUser user;
    private List<OrderDetailItem> orderItems = new ArrayList<>();

    public static OrderDetailDto of(Order order, List<OrderDetailItem> orderItems, OrderDetailUser orderDetailUser) {
        OrderDetailDto orderDetailDto = new OrderDetailDto();
        orderDetailDto.id = order.getId();
        orderDetailDto.orderTime = order.getOrderTime();
        orderDetailDto.orderPrice = order.getOrderPrice();

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
