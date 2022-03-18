package com.justpickup.storeservice.domain.category.web;

import com.justpickup.storeservice.domain.category.dto.CategoryDto;
import com.justpickup.storeservice.domain.category.service.CategoryService;
import com.justpickup.storeservice.global.dto.Result;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer/")
public class CategoryCustomerApiController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<Result> getCategories(@RequestParam("storeId") Long storeId) {
        List<CategoryDto> categoryList = categoryService.getCategoriesWithItemById(storeId);

        GetCategoriesResponse getCategoriesResponse = new GetCategoriesResponse(categoryList);

        return ResponseEntity.ok(Result.createSuccessResult(getCategoriesResponse));
    }

    @Data @NoArgsConstructor
    static class GetCategoriesResponse {
        private List<_Category> categories;

        public GetCategoriesResponse(List<CategoryDto> categoryDtoList) {
            this. categories = categoryDtoList
                    .stream()
                    .map(_Category::new)
                    .collect(Collectors.toList());
        }

        @Data
        static class _Category {
            private Long id;
            private String name;
            private Integer order;
            private List<_Item> items;

            public _Category(CategoryDto categoryDto) {
                List<_Item> items = categoryDto.getItems()
                        .stream()
                        .map(itemDto -> new _Item(itemDto.getId(), itemDto.getName(), itemDto.getPrice()))
                        .collect(Collectors.toList());

                this.id = categoryDto.getId();
                this.name = categoryDto.getName();
                this.order = categoryDto.getOrder();
                this.items = items;
            }
        }

        @Data
        static class _Item {
            private Long id;
            private String name;
            private Long price;

            public _Item(Long id, String name, Long price) {
                this.id = id;
                this.name = name;
                this.price = price;
            }
        }
    }
}
