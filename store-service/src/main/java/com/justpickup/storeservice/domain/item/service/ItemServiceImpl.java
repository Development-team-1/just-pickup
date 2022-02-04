package com.justpickup.storeservice.domain.item.service;

import com.justpickup.storeservice.domain.item.dto.ItemDto;
import com.justpickup.storeservice.domain.item.entity.Item;
import com.justpickup.storeservice.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;


    @Override
    public ItemDto findItemByItemId(Long itemId) {
        Item findItem = itemRepository.findById(itemId)
                .orElseThrow(NoSuchElementException::new);

        return ItemDto.createItemDto(findItem);
    }
}
