package com.justpickup.storeservice.domain.item.dto;

import com.justpickup.storeservice.domain.category.dto.CategoryDto;
import com.justpickup.storeservice.domain.item.entity.Item;
import com.justpickup.storeservice.domain.itemoption.dto.ItemOptionDto;
import com.justpickup.storeservice.domain.itemoption.entity.ItemOption;
import com.justpickup.storeservice.domain.itemoption.entity.OptionType;
import com.justpickup.storeservice.global.entity.Yn;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetItemDto {

    private Long id;

    private String name;

    private Yn salesYn;

    private Long price;

    private List<ItemOptionDto> itemOptions;

    public GetItemDto(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.salesYn = item.getSalesYn();
        this.price = item.getPrice();
        this.itemOptions = item.getItemOptions().stream().
                map(ItemOptionDto::new)
                .collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ItemOptionDto{
        private Long id;

        private OptionType optionType;

        private String name;

        public ItemOptionDto (ItemOption itemOption){
            this.id = itemOption.getId();
            this.optionType = itemOption.getOptionType();
            this.name = itemOption.getName();
        }
    }
}
