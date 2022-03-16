package com.justpickup.storeservice.domain.store.dto;

import com.justpickup.storeservice.domain.store.entity.Store;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreDto {
    private Long id;
    private String name;
    private String phoneNumber;

    @Builder
    public StoreDto(Long id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public static StoreDto of(Store store) {
        StoreDto storeDto = new StoreDto();
        storeDto.id = store.getId();
        storeDto.name = store.getName();
        storeDto.phoneNumber = store.getPhoneNumber();
        return storeDto;
    }
}
