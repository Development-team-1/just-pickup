package com.justpickup.storeservice.domain.item.entity;

import com.justpickup.storeservice.domain.category.entity.Category;
import com.justpickup.storeservice.domain.itemoption.entity.ItemOption;
import com.justpickup.storeservice.domain.store.entity.Store;
import com.justpickup.storeservice.global.entity.BaseEntity;
import com.justpickup.storeservice.global.entity.Photo;
import com.justpickup.storeservice.global.entity.Yn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "item")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Yn salesYn;

    private Long price;

    @Embedded
    private Photo photo;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(mappedBy = "item" ,cascade = {REMOVE,PERSIST} )
    private List<ItemOption> itemOptions;

    // == 연관관계 편의 메소드 ==//
    public void addItemOption(ItemOption itemOption) {
        itemOptions.add(itemOption);
        itemOption.setItem(this);
    }

    public void setStore(Store store) {
        this.store = store;
        store.getItems().add(this);
    }

    public void setCategory(Category category) {
        this.category = category;
        category.getItems().add(this);
    }

    public void setItemNameAndPriceAndCategory(String name , Long price,Category category){
        this.name = name;
        this.price = price;
        this.category = category;
    }

    // == 생성 메소드 == //
    public static Item createdItem(Category category, Store store, List<ItemOption> itemOptions) {
        Item item = new Item();
        item.setCategory(category);
        item.setStore(store);
        itemOptions.forEach(item::addItemOption);
        return item;
    }

    public static Item createdFullItem(Category category, Store store, List<ItemOption> itemOptions, String name , Long price) {
        Item item = new Item();
        item.setItemNameAndPriceAndCategory( name, price ,category);
        item.setStore(store);
        itemOptions.forEach(item::addItemOption);
        return item;
    }
}
