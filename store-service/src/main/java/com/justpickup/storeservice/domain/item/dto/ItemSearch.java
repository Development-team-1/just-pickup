package com.justpickup.storeservice.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemSearch {

    @NotNull
    private String type;

    private String word;


}
