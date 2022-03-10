package com.justpickup.storeservice.domain.category.service;

import com.justpickup.storeservice.domain.category.dto.CategoryDto;
import com.justpickup.storeservice.domain.category.entity.Category;
import com.justpickup.storeservice.domain.category.exception.NotFoundStoreException;
import com.justpickup.storeservice.domain.category.repository.CategoryRepository;
import com.justpickup.storeservice.domain.category.repository.CategoryRepositoryCustom;
import com.justpickup.storeservice.domain.store.entity.Store;
import com.justpickup.storeservice.domain.store.repository.StoreRepository;
import com.justpickup.storeservice.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryRepositoryCustom categoryRepositoryCustom;
    private final StoreRepository storeRepository;

    public List<CategoryDto> getCategoryList(Long userId){

        return categoryRepositoryCustom.getCategoryList(userId)
                .stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void putCategoryList(Long storeId , List<CategoryDto> categoryDtoList , List<CategoryDto> deletedCategoryDtoList ){

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundStoreException(HttpStatus.BAD_REQUEST,"존재하지않는 Store"));

        List<Category> categoryList =categoryDtoList.stream()
                .map(categoryDto -> {
                    categoryDto.setStore(store);
                    return CategoryDto.createCategory(categoryDto);
                })
                .collect(Collectors.toList());

        List<Category> deletedCategoryList =deletedCategoryDtoList.stream()
                .map(categoryDto -> {
                    categoryDto.setStore(store);
                    return CategoryDto.createCategory(categoryDto);
                })
                .collect(Collectors.toList());

        categoryList.forEach(
                category -> {
                    if (category.getId() ==null)
                        categoryRepository.save(category);
                    else {
                        categoryRepository.findById(category.getId())
                                .orElseThrow(() -> new NotFoundStoreException(HttpStatus.BAD_REQUEST,"존재하지않는 Category"))
                                .changeNameAndOrder(category.getName(),category.getOrder());
                    }
                }
        );

        deletedCategoryList.forEach(
                category -> {
                    if (category.getId() !=null)
                        categoryRepository.delete(categoryRepository.findById(category.getId())
                                .orElseThrow(() -> new NotFoundStoreException(HttpStatus.BAD_REQUEST,"존재하지않는 Category")));
                }
        );

    }
}
