package com.justpickup.storeservice.domain.itemoption.repository;

import com.justpickup.storeservice.domain.itemoption.entity.ItemOption;
import com.justpickup.storeservice.domain.itemoption.entity.QItemOption;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.justpickup.storeservice.domain.item.entity.QItem.item;

@Repository
@RequiredArgsConstructor
public class ItemOptionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<ItemOption> findByItem(Long itemId) {

        return queryFactory.selectFrom(QItemOption.itemOption)
                .join(QItemOption.itemOption.item)
                .on(item.id.eq(itemId))
                .fetch();
    }
    

}
