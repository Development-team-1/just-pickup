package com.justpickup.storeservice.domain.item.repository;

import com.justpickup.storeservice.domain.item.entity.Item;
import com.justpickup.storeservice.domain.item.entity.QItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.justpickup.storeservice.domain.category.entity.QCategory.category;
import static com.justpickup.storeservice.domain.item.entity.QItem.item;
import static com.justpickup.storeservice.domain.itemoption.entity.QItemOption.itemOption;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Optional<Item> fetchItem(Long itemId){
        Item fetchItem = queryFactory.selectFrom(item)
                .leftJoin(item.itemOptions, itemOption).fetchJoin()
                .join(item.category,category).fetchJoin()
                .where(item.id.eq(itemId))
                .fetchOne();

        return Optional.ofNullable(fetchItem);
    }

    public List<Item> getItemAndItemOptions(List<Long> itemIds){

        return queryFactory.selectFrom(item)
                .join(item.itemOptions,itemOption).fetchJoin()
                .where(item.id.in(itemIds))
                .distinct()
                .fetch();

    }


    public Page<Item> findItem(Long userId,String word, Pageable pageable){

        //count 가져오기
        Long count =  queryFactory.select(QItem.item.count())
                .from(QItem.item)
                .join(QItem.item.category)
                .join(QItem.item.store)
                .where(
                        QItem.item.name.contains(word)
                            .or(QItem.item.category.name.contains(word)),
                        QItem.item.store.userId.eq(userId)
                )
                .fetchOne();

        //List 가져오기
        List<Item> itemList = queryFactory.selectFrom(QItem.item)
                .join(QItem.item.category).fetchJoin()
                .join(QItem.item.store)
                .where(
                        QItem.item.name.contains(word)
                            .or(QItem.item.category.name.contains(word)),
                        QItem.item.store.userId.eq(userId)
                )
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        return PageableExecutionUtils.getPage(itemList,pageable,() -> count);
    }
}
