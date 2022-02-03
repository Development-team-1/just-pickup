package com.justpickup.storeservice.domain.item.service;

import com.justpickup.storeservice.domain.item.dto.ItemDto;

public interface ItemService {

    ItemDto findItemByItemId(Long itemId);
}
