package com.justpickup.orderservice.domain.order.dto;

import com.justpickup.orderservice.domain.orderItem.entity.OrderItem;
import com.justpickup.orderservice.domain.orderItemOption.dto.OrderItemOptionDto;
import com.justpickup.orderservice.domain.orderItemOption.entity.OrderItemOption;
import com.justpickup.orderservice.global.client.store.GetItemResponse;
import com.justpickup.orderservice.global.client.store.OptionType;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

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

        private List<OrderItemOptionDto> orderItemOptionDtoList;

//        private List<GetItemResponse.ItemOptionDto> orderItemOptionDtoList;

        private Long price;

        private Long count;

        public OrderItemDto(GetItemResponse getItemResponse, OrderItem orderItem) {
            this.id = orderItem.getId();
            this.itemId = getItemResponse.getId();
            this.itemName = getItemResponse.getName();

            //getItemResponse에는 해당 item에 존재하는 itemOption들이 전부 들어있으므로, orderItem에서 orderItemOption에 있는값들을 가져와서 매칭해줌
            this.orderItemOptionDtoList = orderItem.getOrderItemOptions().stream().map(orderItemOption -> {
                OrderItemOptionDto orderItemOptionDto = new OrderItemOptionDto(orderItemOption.getId(), null, null);
                for (GetItemResponse.ItemOptionDto responseItemOption : getItemResponse.getItemOptions()) {

                    if (responseItemOption.getId().equals(orderItemOption.getItemOptionId())) {
                        orderItemOptionDto = new OrderItemOptionDto(orderItemOption.getId(), responseItemOption.getOptionType(), responseItemOption.getName());
                    }
                }
                return orderItemOptionDto;
            }).collect(Collectors.toList());

//            this.orderItemOptionDtoList = getItemResponse.getItemOptions();
            this.price = orderItem.getPrice();
            this.count = orderItem.getCount();
        }

        @Getter
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class OrderItemOptionDto{
            private Long id;

            private OptionType optionType;

            private String name;
        }
    }

}
