package com.justpickup.storeservice.domain.itemoption.dto;

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

    private Long price;

    private String name;

    public ItemOptionDto (ItemOption itemOption){
        this.id = itemOption.getId();
        this.optionType = itemOption.getOptionType();
        this.price = itemOption.getPrice();
        this.name = itemOption.getName();
    }
}
