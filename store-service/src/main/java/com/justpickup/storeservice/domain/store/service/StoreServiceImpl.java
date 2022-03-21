package com.justpickup.storeservice.domain.store.service;

import com.justpickup.storeservice.domain.favoritestore.repository.FavoriteStoreCustom;
import com.justpickup.storeservice.domain.map.entity.Map;
import com.justpickup.storeservice.domain.store.dto.*;
import com.justpickup.storeservice.domain.store.entity.Store;
import com.justpickup.storeservice.domain.store.exception.NotExistStoreException;
import com.justpickup.storeservice.domain.store.repository.StoreRepository;
import com.justpickup.storeservice.domain.store.repository.StoreRepositoryCustom;
import com.justpickup.storeservice.global.entity.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

        return StoreDto.of(store);
    }

    @Override
    public StoreByUserIdDto findStoreByUserId(Long userId) {
        Store store = storeRepository.findByUserId(userId)
                .orElseThrow(() -> new NotExistStoreException(userId + "의 매장은 없습니다."));

        return StoreByUserIdDto.of(store);
    }

    @Override
    public List<StoreDto> findStoreAllById(Iterable<Long> storeIds) {
        return storeRepository.findAllById(storeIds)
                .stream()
                .map(StoreDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public PostStoreDto saveStore(PostStoreDto postStoreDto) {
        PostStoreDto._PostStoreAddress postStoreAddress = postStoreDto.getAddress();
        Address address = new Address(postStoreAddress.getAddress(), postStoreAddress.getZipcode());

        PostStoreDto._PostStoreMap postStoreMap = postStoreDto.getMap();
        Map map = Map.of(postStoreMap.getLatitude(), postStoreMap.getLongitude());

        Store store =
                Store.of(address, map, postStoreDto.getUserId(), postStoreDto.getName(), postStoreDto.getPhoneNumber());

        Store savedStore = storeRepository.save(store);
        return PostStoreDto.of(savedStore);
    }
}
