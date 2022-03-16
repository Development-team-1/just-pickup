package com.justpickup.storeservice.domain.store.service;

import com.justpickup.storeservice.domain.store.dto.SearchStoreCondition;
import com.justpickup.storeservice.domain.store.dto.SearchStoreResult;
import com.justpickup.storeservice.domain.store.dto.StoreByUserIdDto;
import com.justpickup.storeservice.domain.store.dto.StoreDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

public interface StoreService {
    SliceImpl<SearchStoreResult> findSearchStoreScroll(SearchStoreCondition condition, Pageable pageable);
    List<SearchStoreResult> findFavoriteStore(SearchStoreCondition condition, Long userId);
    StoreDto findStoreById(Long storeId);
    StoreByUserIdDto findStoreByUserId(Long userId);
    List<StoreDto> findStoreAllById(Iterable<Long> storeIds);
}
