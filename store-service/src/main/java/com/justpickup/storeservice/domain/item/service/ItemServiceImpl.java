package com.justpickup.storeservice.domain.item.service;

import com.justpickup.storeservice.domain.item.dto.ItemDto;
import com.justpickup.storeservice.domain.item.entity.Item;
import com.justpickup.storeservice.domain.item.exception.NotExistItemException;
import com.justpickup.storeservice.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;


    @Override
    public ItemDto findItemByItemId(Long itemId) {
        Item findItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotExistItemException("존재하지 않는 아이템 입니다."));

        return ItemDto.createItemDto(findItem);
    }
}
