package com.justpickup.storeservice.domain.item.repository;

import com.justpickup.storeservice.domain.item.entity.Item;
import com.justpickup.storeservice.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByStore(Store store);
}
