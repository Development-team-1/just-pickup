package com.justpickup.storeservice.domain.store.service;

import com.justpickup.storeservice.domain.favoritestore.repository.FavoriteStoreCustom;
import com.justpickup.storeservice.domain.store.dto.SearchStoreCondition;
import com.justpickup.storeservice.domain.store.dto.SearchStoreResult;
import com.justpickup.storeservice.domain.store.repository.StoreRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepositoryCustom storeRepositoryCustom;
    private final FavoriteStoreCustom favoriteStoreCustom;

    @Override
    public SliceImpl<SearchStoreResult> findSearchStoreScroll(SearchStoreCondition condition, Pageable pageable) {
        SliceImpl<SearchStoreResult> searchStoreScroll =
                storeRepositoryCustom.findSearchStoreScroll(condition, pageable);

        searchStoreScroll.forEach(result -> {
            Long favoriteCounts = favoriteStoreCustom.countFavoriteStoreByStoreId(result.getStoreId());
            result.setFavoriteCounts(favoriteCounts);
        });

        return searchStoreScroll;
    }
}
