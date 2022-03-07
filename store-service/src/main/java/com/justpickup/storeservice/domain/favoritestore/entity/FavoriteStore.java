package com.justpickup.storeservice.domain.favoritestore.entity;

import com.justpickup.storeservice.domain.store.entity.Store;
import com.justpickup.storeservice.global.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "favorite_store")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteStore extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "favorite_store_id")
    private Long id;

    //== user-service.user pk ==//
    private Long userId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public static FavoriteStore of(Long userId , Store store){
        FavoriteStore favoriteStore = new FavoriteStore();
        favoriteStore.userId = userId;
        favoriteStore.store = store;
        return favoriteStore;
    }
}
