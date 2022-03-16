package com.justpickup.storeservice.domain.item.dto;

import com.justpickup.storeservice.domain.item.entity.Item;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemsDto {
    private Long itemId;
    private String itemName;

    @Builder
    public ItemsDto(Long itemId, String itemName) {
        this.itemId = itemId;
        this.itemName = itemName;
    }

    public static ItemsDto of(Item item) {
        ItemsDto itemsDto = new ItemsDto();
        itemsDto.itemId = item.getId();
        itemsDto.itemName = item.getName();
        return itemsDto;
    }
}
