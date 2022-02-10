package com.justpickup.storeservice.domain.category.web;

import com.justpickup.storeservice.domain.category.dto.CategoryDto;
import com.justpickup.storeservice.domain.category.service.CategoryService;
import com.justpickup.storeservice.global.dto.Result;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/category")
    public ResponseEntity getCategoryList( ){
        // TODO: 2022-02-09 storeId hard coding 변경해야함
        Long storeId = 1L;
        List<CategoryDto> categoryList = categoryService.getCategoryList(storeId);

        List<CategoryResponse> categoryResponseList = categoryList.stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());


        return ResponseEntity.status(HttpStatus.OK)
                .body(Result.createSuccessResult(categoryResponseList));
    }


    @Data
    static class CategoryResponse{
        private Long categoryId;
        private String name;
        private Integer order;

        public  CategoryResponse (CategoryDto categoryDto){
            this.categoryId = categoryDto.getId();
            this.name= categoryDto.getName();
            this.order= categoryDto.getOrder();
        }
    }

    @PutMapping("/category")
    public ResponseEntity putCategoryList( @RequestBody PutCategoryRequest categoryRequest){

        List<CategoryDto> categoryList = categoryRequest.getCategoryList().stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());

        List<CategoryDto> deletedList = categoryRequest.getDeletedList().stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());

        categoryService.putCategoryList(categoryRequest.getStoreId(),categoryList,deletedList);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(Result.createSuccessResult(null));
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class PutCategoryRequest{
        private Long storeId;
        private List<Category> categoryList;
        private List<Category> deletedList;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Category{
            private Long categoryId;
            private String name;
            private Integer order;
        }

    }

}
