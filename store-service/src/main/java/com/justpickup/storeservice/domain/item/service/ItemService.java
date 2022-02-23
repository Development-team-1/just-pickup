package com.justpickup.storeservice.domain.item.service;

import com.justpickup.storeservice.domain.item.dto.ItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {

    ItemDto findItemByItemId(Long itemId);

    Page<ItemDto> findItemList(Long storeId,String word, Pageable pageable);
}
