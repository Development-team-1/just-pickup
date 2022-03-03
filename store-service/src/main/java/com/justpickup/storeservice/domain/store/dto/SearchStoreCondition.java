package com.justpickup.storeservice.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter @AllArgsConstructor
public class SearchStoreCondition {
    @NotNull(message = "필수 값입니다.")
    private double latitude;
    @NotNull(message = "필수 값입니다.")
    private double longitude;
    private String storeName;
}
