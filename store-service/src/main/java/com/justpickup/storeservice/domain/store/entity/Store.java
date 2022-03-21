package com.justpickup.storeservice.domain.store.entity;

import com.justpickup.storeservice.domain.category.entity.Category;
import com.justpickup.storeservice.domain.favoritestore.entity.FavoriteStore;
import com.justpickup.storeservice.domain.item.entity.Item;
import com.justpickup.storeservice.domain.map.entity.Map;
import com.justpickup.storeservice.global.entity.Address;
import com.justpickup.storeservice.global.entity.BaseEntity;
import com.justpickup.storeservice.global.entity.Photo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "store")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "store_id")
    private Long id;

    private LocalDateTime businessStartTime;

    private LocalDateTime businessEndTime;

    private String name;

    private String phoneNumber;

    @Embedded
    private Address address;

    @Embedded
    private Photo photo;

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "map_id")
    private Map map;

    //== user-service.user pk ==//
    private Long userId;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<FavoriteStore> favoriteStores = new ArrayList<>();

    // == 연관관계 편의 메소드 == //
    public void addCategory(Category category) {
        categories.add(category);
        category.setStore(this);
    }

    public void addItem(Item item) {
        items.add(item);
        item.setStore(this);
    }

    public static Store of(Address address, Map map, Long userId, String name, String phoneNumber) {
        Store store = new Store();
        store.address = address;
        store.map = map;
        store.userId = userId;
        store.name = name;
        store.phoneNumber = phoneNumber;
        return store;
    }
}
