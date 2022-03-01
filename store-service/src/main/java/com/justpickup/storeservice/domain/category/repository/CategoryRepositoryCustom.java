package com.justpickup.storeservice.domain.category.repository;

import com.justpickup.storeservice.domain.category.entity.Category;
import com.justpickup.storeservice.domain.category.entity.QCategory;
import com.justpickup.storeservice.domain.item.entity.QItem;
import com.justpickup.storeservice.domain.store.entity.QStore;
import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Category> getCategoryList(Long storeId){

        List<Category> categoryList = jpaQueryFactory.selectFrom(QCategory.category)
                .leftJoin(QCategory.category.items, QItem.item).fetchJoin()
                .join(QCategory.category.store, QStore.store).fetchJoin()
                .where(QCategory.category.store.id.eq(storeId))
                .orderBy(QCategory.category.order.asc())
                .distinct()
                .fetch();

        return categoryList;
    }





}
