package com.justpickup.storeservice.domain.category.web;

import com.justpickup.storeservice.domain.category.dto.CategoryDto;
import com.justpickup.storeservice.domain.category.service.CategoryService;
import com.justpickup.storeservice.domain.item.dto.ItemDto;
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
@RequestMapping("/api/owner")
public class CategoryOwnerApiController {

    private final CategoryService categoryService;

    @GetMapping("/category")
    public ResponseEntity getCategoryList(@RequestHeader(value = "user-id") String userHeader){
        Long userId = Long.parseLong(userHeader);
        List<CategoryDto> categoryList = categoryService.getCategoriesWithItemByUserId(userId);

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
        private List<ItemResponse> items;

        public  CategoryResponse (CategoryDto categoryDto){
            this.categoryId = categoryDto.getId();
            this.name= categoryDto.getName();
            this.order= categoryDto.getOrder();
            this.items = categoryDto.getItems()!=null
                    ?categoryDto.getItems().stream()
                    .map(ItemResponse::new)
                    .collect(Collectors.toList())
                    :null;
        }

        @Data
        static class ItemResponse{
            private Long id;
            private String name;

            public ItemResponse(ItemDto itemDto) {
                this.id = itemDto.getId();
                this.name = itemDto.getName();
            }

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
