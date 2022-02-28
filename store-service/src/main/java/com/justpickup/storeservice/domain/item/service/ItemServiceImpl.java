package com.justpickup.storeservice.domain.item.service;

import com.justpickup.storeservice.domain.category.entity.Category;
import com.justpickup.storeservice.domain.category.repository.CategoryRepository;
import com.justpickup.storeservice.domain.item.dto.ItemDto;
import com.justpickup.storeservice.domain.item.entity.Item;
import com.justpickup.storeservice.domain.item.exception.NotExistItemException;
import com.justpickup.storeservice.domain.item.repository.ItemRepository;
import com.justpickup.storeservice.domain.item.repository.ItemRepositoryCustom;
import com.justpickup.storeservice.domain.itemoption.dto.ItemOptionDto;
import com.justpickup.storeservice.domain.itemoption.entity.ItemOption;
import com.justpickup.storeservice.domain.itemoption.repository.ItemOptionRepository;
import com.justpickup.storeservice.domain.store.entity.Store;
import com.justpickup.storeservice.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemRepositoryCustom itemRepositoryCustom;
    private final ItemOptionRepository itemOptionRepository;
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;



    @Override
    public ItemDto findItemByItemId(Long itemId) {
        Item findItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotExistItemException("존재하지 않는 아이템 입니다."));

        return ItemDto.createWithCategoryItemDtoAndItemOption(findItem);
    }

    @Override
    public Page<ItemDto> findItemList( Long storeId,String word, Pageable pageable) {

        Page<Item> itemList = itemRepositoryCustom.findItem(storeId,word,pageable);
        return PageableExecutionUtils.getPage(itemList.stream()
                .map(ItemDto::createWithCategoryItemDto)
                .collect(Collectors.toList()),pageable,itemList::getTotalElements);
    }

    @Override
    @Transactional
    public void putItem(Long itemId,
                        String itemName,
                        Long itemPrice,
                        Long categoryId,
                        List<ItemOptionDto> itemOptionDtos) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotExistItemException("존재하지 않는 아이템 입니다."));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotExistItemException("존재하지 않는 카테고리 입니다."));

        item.setItemNameAndPriceAndCategory(itemName,itemPrice,category);

        itemOptionDtos
                .forEach(itemOptionDto -> {
                    if (itemOptionRepository.existsById(itemOptionDto.getId()))
                            itemOptionRepository.save(ItemOptionDto.createItemOption(itemOptionDto, item));
                });
    }

    @Override
    @Transactional
    public void createItem(Long storeId,
                              String itemName,
                              Long itemPrice,
                              Long categoryId,
                              List<ItemOptionDto> itemOptionDtos) {

        //find Store
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotExistItemException("존재하지 않는 매장 입니다."));

        //find Category
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotExistItemException("존재하지 않는 카테고리 입니다."));

        //create Item
        Item item = Item.createdFullItem(category,store,new ArrayList<>() ,itemName, itemPrice);

        //add ItemOption
        itemOptionDtos.forEach((itemOptionDto ->
                itemOptionRepository.save(ItemOptionDto.createItemOption(itemOptionDto, item))));
    }
}
