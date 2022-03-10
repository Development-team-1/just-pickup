package com.justpickup.storeservice.domain.store.repository;

import com.justpickup.storeservice.domain.favoritestore.entity.QFavoriteStore;
import com.justpickup.storeservice.domain.store.dto.SearchStoreCondition;
import com.justpickup.storeservice.domain.store.dto.SearchStoreResult;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.justpickup.storeservice.domain.store.entity.QStore.store;
import static com.querydsl.core.types.dsl.MathExpressions.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class StoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public SliceImpl<SearchStoreResult> findSearchStoreScroll(SearchStoreCondition condition, Pageable pageable) {
        NumberExpression<Double> haversineDistance = getHaversineDistance(condition.getLatitude(),condition.getLongitude());
        NumberPath<Double> distanceAlias = Expressions.numberPath(Double.class, "distance");

        List<SearchStoreResult> content = queryFactory.select(
                        Projections.constructor(SearchStoreResult.class,
                                store.id,
                                store.name,
                                haversineDistance.as(distanceAlias))
                )
                .from(store)
                .join(store.map)
                .where(
                        storeNameContains(condition.getStoreName())
                )
                .orderBy(distanceAlias.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    private BooleanExpression storeNameContains(String storeName) {
        return storeName == null ? null : store.name.contains(storeName);
    }

    public List<SearchStoreResult> findFavoriteStore(SearchStoreCondition condition,Long userId){

        NumberExpression<Double> haversineDistance = getHaversineDistance(condition.getLatitude(),condition.getLongitude());
        NumberPath<Double> distanceAlias = Expressions.numberPath(Double.class, "distance");
        List<SearchStoreResult> content = queryFactory.select(
                        Projections.constructor(SearchStoreResult.class,
                                store.id,
                                store.name,
                                haversineDistance.as(distanceAlias))
                )
                .from(store)
                .join(store.map)
                .join(store.favoriteStores, QFavoriteStore.favoriteStore)
                .on(QFavoriteStore.favoriteStore.userId.eq(userId))
                .orderBy(distanceAlias.asc())
                .fetch();


        return content;
    }

    private NumberExpression<Double> getHaversineDistance(double SearchLatitude, double SearchLongitude){
        Expression<Double> latitude = Expressions.constant(SearchLatitude);
        Expression<Double> longitude = Expressions.constant(SearchLongitude);

        int earthRadius = 6371;

        NumberExpression<Double> haversineDistance = acos(
                cos(radians(latitude))
                        .multiply(cos(radians(store.map.latitude)))
                        .multiply(
                                cos(radians(store.map.longitude)
                                        .subtract(radians(longitude)))
                        )
                        .add(
                                sin(radians(latitude))
                                        .multiply(sin(radians(store.map.latitude)))
                        )
        )
                .multiply(Expressions.constant(earthRadius * 1000));
        return haversineDistance;
    }

}
