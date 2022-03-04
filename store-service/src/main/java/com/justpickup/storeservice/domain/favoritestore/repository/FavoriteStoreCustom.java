package com.justpickup.storeservice.domain.favoritestore.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.justpickup.storeservice.domain.favoritestore.entity.QFavoriteStore.favoriteStore;

@Repository
@RequiredArgsConstructor
public class FavoriteStoreCustom {

    private final JPAQueryFactory queryFactory;

    public Long countFavoriteStoreByStoreId(Long storeId) {
        return queryFactory.select(favoriteStore.count())
                .from(favoriteStore)
                .join(favoriteStore.store)
                .where(favoriteStore.store.id.eq(storeId))
                .fetchOne();
    }
}
