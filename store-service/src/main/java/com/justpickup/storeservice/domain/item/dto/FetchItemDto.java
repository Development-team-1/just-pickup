package com.justpickup.storeservice.domain.item.dto;

import com.justpickup.storeservice.domain.category.dto.CategoryDto;
import com.justpickup.storeservice.domain.item.entity.Item;
import com.justpickup.storeservice.domain.itemoption.dto.ItemOptionDto;
import com.justpickup.storeservice.domain.store.dto.StoreDto;
import com.justpickup.storeservice.global.entity.Yn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FetchItemDto {

    private Long id;

    private String name;

    private Yn salesYn;

    private Long price;

    private CategoryDto categoryDto;

    private List<ItemOptionDto> itemOptions;

    private StoreDto storeDto;

    public FetchItemDto(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.salesYn = item.getSalesYn();
        this.price = item.getPrice();
        this.categoryDto = new CategoryDto(item.getCategory());
        this.itemOptions = item.getItemOptions().stream().map(ItemOptionDto::new).collect(Collectors.toList());
        this.storeDto = new StoreDto(item.getStore().getId(), item.getStore().getName(), item.getStore().getPhoneNumber());
    }

}
