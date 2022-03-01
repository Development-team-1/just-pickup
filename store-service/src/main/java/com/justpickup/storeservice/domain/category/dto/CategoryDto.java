package com.justpickup.storeservice.domain.category.dto;

import com.justpickup.storeservice.domain.category.entity.Category;
import com.justpickup.storeservice.domain.category.web.CategoryController;
import com.justpickup.storeservice.domain.item.dto.ItemDto;
import com.justpickup.storeservice.domain.item.entity.Item;
import com.justpickup.storeservice.domain.store.entity.Store;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private Long id;
    private String name;
    private Integer order;
    private Store store;
    private List<ItemDto> items;


    public CategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.order = category.getOrder();
        this.items = category.getItems().stream()
                .map(ItemDto::createItemDto)
                .collect(Collectors.toList());
    }

    public CategoryDto(CategoryController.PutCategoryRequest.Category category) {
        this.id = category.getCategoryId();
        this.name = category.getName();
        this.order = category.getOrder();
    }

    public static Category createCategory(CategoryDto categoryDto){
        Category category = Category.createCategory(
                categoryDto.getId()
                ,categoryDto.getName()
                , categoryDto.getOrder()
                , categoryDto.getStore());
        return category;
    }

    public void setStore(Store store){
        this.store=store;
    }
}
