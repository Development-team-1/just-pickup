package com.justpickup.storeservice.domain.store.repository;

import com.justpickup.storeservice.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store,Long> {

    Optional<Store> findByUserId(Long userId);
}
