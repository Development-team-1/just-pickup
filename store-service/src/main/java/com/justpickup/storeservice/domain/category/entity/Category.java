package com.justpickup.storeservice.domain.category.entity;

import com.justpickup.storeservice.domain.item.entity.Item;
import com.justpickup.storeservice.domain.store.entity.Store;
import com.justpickup.storeservice.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "category")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @Column(name = "orders")
    private Integer order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(mappedBy = "category")
    private List<Item> items;

    // == 연관관계 편의 메소드 == //
    public void setStore(Store store) {
        this.store = store;
        store.getCategories().add(this);
    }

    public void addItem(Item item) {
        items.add(item);
        item.setCategory(this);
    }

    public void changeNameAndOrder(String name , Integer order){
        this.name = name;
        this.order = order;
    }

    private Category (Long id , String name, Integer order, Store store){
        this.id = id;
        this.name = name;
        this.order = order;
        this.store = store;
    }

    public static Category createCategory(Long id ,String name, Integer order, Store store){
        return new Category(id,name,order,store);
    }

}
