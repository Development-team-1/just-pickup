package com.justpickup.storeservice.domain.item.repository;

import com.justpickup.storeservice.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
