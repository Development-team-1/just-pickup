package com.justpickup.storeservice.domain.itemoption.service;

import com.justpickup.storeservice.domain.item.dto.ItemDto;
import com.justpickup.storeservice.domain.itemoption.dto.ItemOptionDto;
import com.justpickup.storeservice.domain.itemoption.repository.ItemOptionRepository;
import com.justpickup.storeservice.domain.itemoption.repository.ItemOptionRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemOptionService {
    private final ItemOptionRepositoryCustom itemOptionRepositoryCustom;

    public List<ItemOptionDto> getItemOption( ItemDto itemDto){
        return itemOptionRepositoryCustom.findByItem(itemDto.getId())
                .stream().map(ItemOptionDto::new)
                .collect(Collectors.toList());
    }
}
