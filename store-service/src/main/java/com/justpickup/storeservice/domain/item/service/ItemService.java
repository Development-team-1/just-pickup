package com.justpickup.storeservice.domain.item.service;

import com.justpickup.storeservice.domain.item.dto.FetchItemDto;
import com.justpickup.storeservice.domain.item.dto.GetItemDto;
import com.justpickup.storeservice.domain.item.dto.ItemDto;
import com.justpickup.storeservice.domain.item.dto.ItemsDto;
import com.justpickup.storeservice.domain.itemoption.dto.ItemOptionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {

    ItemDto findItemByItemId(Long itemId);

    List<GetItemDto> getItemAndItemOptions(List<Long> itemIds);
    FetchItemDto fetchItem(Long itemId);

    Page<ItemDto> findMenuItemList(Long userId, String word, Pageable pageable);

    void putItem(Long itemId, String itemName, Long itemPrice, Long categoryId, List<ItemOptionDto> itemOptionDtos);

    void createItem( Long userId, String itemName, Long itemPrice, Long categoryId, List<ItemOptionDto> itemOptionDtos);

    List<ItemsDto> findItems(List<Long> itemIds);
}
