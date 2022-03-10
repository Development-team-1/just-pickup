package com.justpickup.storeservice.domain.itemoption.dto;

import com.justpickup.storeservice.domain.item.entity.Item;
import com.justpickup.storeservice.domain.itemoption.entity.ItemOption;
import com.justpickup.storeservice.domain.itemoption.entity.OptionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemOptionDto {

    private Long id;

    private OptionType optionType;

    private String name;

    public ItemOptionDto (ItemOption itemOption){
        this.id = itemOption.getId();
        this.optionType = itemOption.getOptionType();
        this.name = itemOption.getName();
    }

    public static ItemOption createItemOption (ItemOptionDto itemOptionDto, Item item){
        return new ItemOption(itemOptionDto.getOptionType(), itemOptionDto.getName(),item);
    }
}
