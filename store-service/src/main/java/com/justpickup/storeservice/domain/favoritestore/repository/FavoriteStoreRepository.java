package com.justpickup.storeservice.domain.favoritestore.repository;

import com.justpickup.storeservice.domain.favoritestore.entity.FavoriteStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteStoreRepository extends JpaRepository<FavoriteStore,Long> {
}
