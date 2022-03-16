package com.justpickup.storeservice.domain.store.dto;

import com.justpickup.storeservice.domain.store.entity.Store;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StoreByUserIdDto {
    private Long id;
    private String name;

    @Builder
    public StoreByUserIdDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StoreByUserIdDto of(Store store) {
        return StoreByUserIdDto.builder()
                .id(store.getId())
                .name(store.getName())
                .build();
    }
}
