package com.justpickup.storeservice.domain.store.dto;

import com.justpickup.storeservice.domain.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class StoreDto {
    private Long id;
    private String name;
    private String phoneNumber;

    public StoreDto(Store store) {
        this.id = store.getId();
        this.name = store.getName();
        this.phoneNumber = store.getPhoneNumber();
    }

}
