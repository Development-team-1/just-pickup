package com.justpickup.storeservice.domain.favoritestore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetFavoriteStoreByStoreIdDto {

    private Long userId;
    private Long storeId;
    private boolean isExist;

}
