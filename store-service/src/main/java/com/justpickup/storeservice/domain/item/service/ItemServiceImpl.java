package com.justpickup.storeservice.domain.item.service;

import com.justpickup.storeservice.domain.category.entity.Category;
import com.justpickup.storeservice.domain.category.repository.CategoryRepository;
import com.justpickup.storeservice.domain.item.dto.FetchItemDto;
import com.justpickup.storeservice.domain.item.dto.GetItemDto;
import com.justpickup.storeservice.domain.item.dto.ItemDto;
import com.justpickup.storeservice.domain.item.dto.ItemsDto;
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
    public List<GetItemDto> getItemAndItemOptions(List<Long> itemIds) {

        List<Item> items = itemRepositoryCustom.getItemAndItemOptions(itemIds);
        return items.stream()
                .map(GetItemDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public FetchItemDto fetchItem(Long itemId) {
        Item findItem = itemRepositoryCustom.fetchItem(itemId)
                .orElseThrow(() -> new NotExistItemException("존재하지 않는 아이템 입니다."));

        return new FetchItemDto(findItem);
    }


    @Override
    public Page<ItemDto> findMenuItemList(Long userId, String word, Pageable pageable) {

        Page<Item> itemList = itemRepositoryCustom.findItem(userId, word, pageable);
        return PageableExecutionUtils.getPage(itemList.stream()
                .map(ItemDto::createWithCategory)
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


        //item에 해당하는 itemoption  전부조회
        List<ItemOption> byItem = itemOptionRepository.findByItem(item);

        //itemOptionDtos 없는 itemOption 전부 삭제
        byItem.forEach(itemOption -> {
            boolean isDeleted = true;
            for ( ItemOptionDto itemOptionDto: itemOptionDtos) {
                if(itemOption.getId().equals(itemOptionDto.getId())) isDeleted = false;
            }
            if(isDeleted) itemOptionRepository.delete(itemOption);
        });

        //id가 없으면 저장
        itemOptionDtos
                .forEach(itemOptionDto -> {
                    if(itemOptionDto.getId()==null)
                            itemOptionRepository.save(ItemOptionDto.createItemOption(itemOptionDto, item));
                });
    }

    @Override
    @Transactional
    public void createItem(Long userId,
                              String itemName,
                              Long itemPrice,
                              Long categoryId,
                              List<ItemOptionDto> itemOptionDtos) {
        //find Store
        Store store = storeRepository.findByUserId(userId)
                .orElseThrow(() -> new NotExistItemException("존재하지 않는 매장 입니다."));

        //find Category
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotExistItemException("존재하지 않는 카테고리 입니다."));

        List<ItemOption> itemOptions = itemOptionDtos
                .stream()
                .map(itemOptionDto -> ItemOption.of(itemOptionDto.getOptionType(), itemOptionDto.getName()))
                .collect(Collectors.toList());

        Item createdItem = Item.of(itemName, itemPrice, category, store, itemOptions);

        itemRepository.save(createdItem);
    }

    @Override
    public List<ItemsDto> findItems(List<Long> itemIds) {
        return itemRepository.findAllById(itemIds)
                .stream()
                .map(ItemsDto::of)
                .collect(Collectors.toList());
    }
}
