package com.justpickup.orderservice.domain.orderItem.dto;

import com.justpickup.orderservice.domain.orderItem.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
public class OrderItemDto {

    private Long id;

    private Long itemId;

    private String itemName;

    // private List<OrderItemOptionDto> orderItemOptionDtoList;

    private Long price;

    private Long count;

    // == 생성 메소드 == //
    @Builder
    public OrderItemDto(Long id, Long itemId, Long price, Long count) {
        this.id = id;
        this.itemId = itemId;
        this.price = price;
        this.count = count;
    }

    public static OrderItemDto createPrimitiveField(OrderItem orderItem) {
        return OrderItemDto.builder()
                .id(orderItem.getId())
                .itemId(orderItem.getItemId())
                .price(orderItem.getPrice())
                .count(orderItem.getCount())
                .build();
    }

    // == 변수 변경 메소드 == //
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
