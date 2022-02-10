package com.justpickup.storeservice.domain.category.repository;

import com.justpickup.storeservice.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByStoreIdOrderByOrder(Long storeId);
}
