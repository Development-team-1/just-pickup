package com.justpickup.storeservice.domain.item.dto;

import com.justpickup.storeservice.domain.item.entity.Item;
import lombok.Getter;

@Getter
public class ItemsDto {
    private Long itemId;
    private String itemName;

    public static ItemsDto of(Item item) {
        ItemsDto itemsDto = new ItemsDto();
        itemsDto.itemId = item.getId();
        itemsDto.itemName = item.getName();
        return itemsDto;
    }
}
