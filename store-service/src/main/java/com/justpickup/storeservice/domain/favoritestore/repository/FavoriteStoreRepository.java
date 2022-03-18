package com.justpickup.storeservice.domain.favoritestore.repository;

import com.justpickup.storeservice.domain.favoritestore.entity.FavoriteStore;
import com.justpickup.storeservice.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteStoreRepository extends JpaRepository<FavoriteStore,Long> {

    Optional<FavoriteStore> findByUserIdAndStore(Long userId, Store store);
}
