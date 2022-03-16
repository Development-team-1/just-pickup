package com.justpickup.orderservice.domain.order.dto;

import com.justpickup.orderservice.domain.orderItem.entity.OrderItem;
import com.justpickup.orderservice.global.client.store.GetItemResponse;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FetchOrderDto {
    private Long id;

    private Long userId;

    private Long orderPrice;

    private String storeName;

    private List<OrderItemDto> orderItemDtoList;

    @Getter
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemDto{

        private Long id;

        private Long itemId;

        private String itemName;

        private List<GetItemResponse.ItemOptionDto> orderItemOptionDtoList;

        private Long price;

        private Long count;

        public OrderItemDto(GetItemResponse getItemResponse, OrderItem orderItem) {
            this.id = orderItem.getId();
            this.itemId = getItemResponse.getId();
            this.itemName = getItemResponse.getName();
            this.orderItemOptionDtoList = getItemResponse.getItemOptions();
            this.price = orderItem.getPrice();
            this.count = orderItem.getCount();
        }
    }

}
