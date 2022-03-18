package com.justpickup.storeservice.domain.favoritestore.service;

import com.justpickup.storeservice.domain.favoritestore.dto.GetFavoriteStoreByStoreIdDto;
import com.justpickup.storeservice.domain.favoritestore.entity.FavoriteStore;
import com.justpickup.storeservice.domain.favoritestore.repository.FavoriteStoreRepository;
import com.justpickup.storeservice.domain.store.entity.Store;
import com.justpickup.storeservice.domain.store.exception.NotExistStoreException;
import com.justpickup.storeservice.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@RequiredArgsConstructor
@Service
public class FavoriteStoreService {

    private final FavoriteStoreRepository favoriteStoreRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void patchFavoriteStore(Long userId, Long storeId){
        Store store = storeRepository.findById(storeId)
                .orElseThrow(()-> new NotExistStoreException("매장이 존재하지않습니다."));

        favoriteStoreRepository
                .findByUserIdAndStore(userId, store)
                .ifPresentOrElse(
                        favoriteStoreRepository::delete,
                        ()->favoriteStoreRepository.save(FavoriteStore.of(userId, store))
                );

    }

    @Transactional
    public GetFavoriteStoreByStoreIdDto getFavoriteStoreByStoreId(Long userId, Long storeId){
        Store store = storeRepository.findById(storeId)
                .orElseThrow(()-> new NotExistStoreException("매장이 존재하지않습니다."));

        Optional<FavoriteStore> byUserIdAndStore = favoriteStoreRepository
                .findByUserIdAndStore(userId, store);

        if(byUserIdAndStore.isPresent()){
            return new GetFavoriteStoreByStoreIdDto(userId,storeId, true);
        }else {
            return new GetFavoriteStoreByStoreIdDto(userId,storeId, false);
        }

    }

}
