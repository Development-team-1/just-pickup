package com.justpickup.storeservice.domain.item.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.justpickup.storeservice.domain.item.dto.ItemDto;
import com.justpickup.storeservice.domain.item.service.ItemService;
import com.justpickup.storeservice.domain.itemoption.dto.ItemOptionDto;
import com.justpickup.storeservice.domain.itemoption.entity.ItemOption;
import com.justpickup.storeservice.domain.itemoption.entity.OptionType;
import com.justpickup.storeservice.global.dto.Result;
import com.justpickup.storeservice.global.entity.Yn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/item")
    public ResponseEntity<Result<GetItemResponse>> getItemList( @RequestParam String word,
                                                               @PageableDefault(page = 0, size = 10) Pageable pageable){

        Long storeId = 1L;

        Page<ItemDto> itemDtoList = itemService.findItemList(storeId,word,pageable);
        List<GetItemListResponse.Item> itemList = itemDtoList.stream()
                .map(GetItemListResponse.Item::new)
                .collect(Collectors.toList());

        GetItemListResponse getItemResponse = new GetItemListResponse(
                itemList,
                itemDtoList.getNumber(),
                itemDtoList.getTotalPages()
        );


        return ResponseEntity.status(HttpStatus.OK)
                .body((Result<GetItemResponse>)Result.createSuccessResult(getItemResponse));
    }


    @Data @NoArgsConstructor @AllArgsConstructor
    static class GetItemListResponse {
        private List<Item> itemList;
        private Page page;

        public GetItemListResponse(List<Item> itemList, int startPage,int totalPage) {
            this.itemList = itemList;
            this.page = new Page(startPage,totalPage);
        }

        @Data
        static class Item{
            private Long id;
            private String name;
            private Yn salesYn;
            private Long price;
            private String categoryName;

            public Item(ItemDto itemDto) {
                this.id = itemDto.getId();
                this.name = itemDto.getName();
                this.salesYn = itemDto.getSalesYn();
                this.price = itemDto.getPrice();
                this.categoryName = itemDto.getCategoryDto().getName();
            }
        }

        @Data @AllArgsConstructor
        static class Page {
            int startPage;
            int totalPage;
        }
    }


    @GetMapping("/item/{itemId}")
    public ResponseEntity getItem(@PathVariable("itemId") Long itemId) {
        ItemDto itemByItemId = itemService.findItemByItemId(itemId);

        GetItemResponse getItemResponse = new GetItemResponse(itemByItemId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Result.createSuccessResult(getItemResponse));
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    static class GetItemResponse {
        private Long id;
        private String name;
        private Yn salesYn;
        private Long price;
        private String CategoryName;
        private List<ItemOptionResponse> itemOptions;

        public GetItemResponse(ItemDto itemDto) {
            this.id = itemDto.getId();
            this.name = itemDto.getName();
            this.salesYn = itemDto.getSalesYn();
            this.price = itemDto.getPrice();
            this.CategoryName = itemDto.getCategoryDto().getName();
            this.itemOptions = itemDto.getItemOptions()
                    .stream().map(ItemOptionResponse::new)
                    .collect(Collectors.toList());
        }

        @Data
        static class ItemOptionResponse{
            private Long id;

            private OptionType optionType;

            private Long price;

            private String name;

            public ItemOptionResponse(ItemOptionDto itemOptionDto) {

                this.id = itemOptionDto.getId();
                this.optionType = itemOptionDto.getOptionType();
                this.price = itemOptionDto.getPrice();
                this.name = itemOptionDto.getName();
            }
        }
    }
}
