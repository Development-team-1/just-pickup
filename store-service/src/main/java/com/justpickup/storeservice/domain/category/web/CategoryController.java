package com.justpickup.storeservice.domain.category.web;

import com.justpickup.storeservice.domain.category.dto.CategoryDto;
import com.justpickup.storeservice.domain.category.service.CategoryService;
import com.justpickup.storeservice.global.dto.Result;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/category")
    public ResponseEntity getCategoryList( ){
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
        private String name;
        private Integer order;

        public  CategoryResponse (CategoryDto categoryDto){
            this.name= categoryDto.getName();
            this.order= categoryDto.getOrder();
        }
    }

}
