package com.justpickup.storeservice.domain.category.dto;

import com.justpickup.storeservice.domain.category.entity.Category;
import com.justpickup.storeservice.domain.item.entity.Item;
import com.justpickup.storeservice.domain.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private String name;
    private Integer order;
    private Store store;
    private List<Item> items;

    public CategoryDto(Category category) {
        this.name = category.getName();
        this.order = category.getOrder();
        this.store = category.getStore();
        this.items = category.getItems();
        this.name = category.getName();
    }
}
