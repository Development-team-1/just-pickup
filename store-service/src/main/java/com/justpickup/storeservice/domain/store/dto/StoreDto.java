package com.justpickup.storeservice.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreDto {
    private Long storeId;
    private String storeName;
}
