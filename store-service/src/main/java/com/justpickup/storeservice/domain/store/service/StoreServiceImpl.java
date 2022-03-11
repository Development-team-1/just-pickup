package com.justpickup.storeservice.domain.store.service;

import com.justpickup.storeservice.domain.favoritestore.repository.FavoriteStoreCustom;
import com.justpickup.storeservice.domain.store.dto.SearchStoreCondition;
import com.justpickup.storeservice.domain.store.dto.SearchStoreResult;
import com.justpickup.storeservice.domain.store.dto.StoreByUserIdDto;
import com.justpickup.storeservice.domain.store.dto.StoreDto;
import com.justpickup.storeservice.domain.store.entity.Store;
import com.justpickup.storeservice.domain.store.exception.NotExistStoreException;
import com.justpickup.storeservice.domain.store.repository.StoreRepository;
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
    private final FavoriteStoreCustom favoriteStoreCustom;
    private final StoreRepository storeRepository;

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

    @Override
    public List<SearchStoreResult> findFavoriteStore(SearchStoreCondition condition, Long userId) {
        List<SearchStoreResult> favoriteStores = storeRepositoryCustom.findFavoriteStore(condition, userId);
        favoriteStores.forEach(result -> {
            Long favoriteCounts = favoriteStoreCustom.countFavoriteStoreByStoreId(result.getStoreId());
            result.setFavoriteCounts(favoriteCounts);
        });
        return favoriteStores;
    }

    @Override
    public StoreDto findStoreById(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotExistStoreException(storeId + "는 없는 매장 고유번호입니다."));

        return new StoreDto(store);
    }

    @Override
    public StoreByUserIdDto findStoreByUserId(Long userId) {
        Store store = storeRepository.findByUserId(userId)
                .orElseThrow(() -> new NotExistStoreException(userId + "의 매장은 없습니다."));

        return StoreByUserIdDto.of(store);
    }
}
