package com.justpickup.storeservice.domain.store.service;

import com.justpickup.storeservice.domain.store.dto.SearchStoreCondition;
import com.justpickup.storeservice.domain.store.dto.SearchStoreResult;
import com.justpickup.storeservice.domain.store.repository.StoreRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepositoryCustom storeRepositoryCustom;

    @Override
    public SliceImpl<SearchStoreResult> findSearchStoreScroll(SearchStoreCondition condition, Pageable pageable) {
        return storeRepositoryCustom.findSearchStoreScroll(condition, pageable);
    }

    @Override
    public List<SearchStoreResult> findFavoriteStore(SearchStoreCondition condition, Long userId) {
        return storeRepositoryCustom.findFavoriteStore(condition,userId);
    }
}
