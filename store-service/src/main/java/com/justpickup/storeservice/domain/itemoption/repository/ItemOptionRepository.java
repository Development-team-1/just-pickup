package com.justpickup.storeservice.domain.itemoption.repository;

import com.justpickup.storeservice.domain.item.entity.Item;
import com.justpickup.storeservice.domain.itemoption.entity.ItemOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ItemOptionRepository extends JpaRepository<ItemOption,Long> {

    List<ItemOption> findByItem(Item item);
}
