package com.justpickup.storeservice.domain.item.dto;

import com.justpickup.storeservice.domain.category.dto.CategoryDto;
import com.justpickup.storeservice.domain.item.entity.Item;
import com.justpickup.storeservice.domain.itemoption.dto.ItemOptionDto;
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
public class ItemDto {

    private Long id;

    private String name;

    private Yn salesYn;

    private Long price;

    private CategoryDto categoryDto;

    private List<ItemOptionDto> itemOptions;

    /*
     private PhotoDto photoDto;
     private StoreDto storeDto;
    */

    // == 생성 메소드 == //
    public ItemDto(Long id, String name, Yn salesYn, Long price) {
        this.id = id;
        this.name = name;
        this.salesYn = salesYn;
        this.price = price;
    }

    public static ItemDto createItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .salesYn(item.getSalesYn())
                .build();
    }

    public static ItemDto createWithCategory(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .categoryDto(new CategoryDto(item.getCategory()))
                .price(item.getPrice())
                .salesYn(item.getSalesYn())
                .build();
    }

    public static ItemDto createWithCategoryItemDtoAndItemOption(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .categoryDto(CategoryDto.builder()
                        .id(item.getCategory().getId())
                        .name(item.getCategory().getName())
                        .order(item.getCategory().getOrder())
                        .build())
                .price(item.getPrice())
                .salesYn(item.getSalesYn())
                .itemOptions(item.getItemOptions()
                        .stream().map(ItemOptionDto::new)
                        .collect(Collectors.toList()))
                .build();
    }

}
