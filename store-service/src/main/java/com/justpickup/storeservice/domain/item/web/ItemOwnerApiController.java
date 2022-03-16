package com.justpickup.storeservice.domain.item.web;

import com.justpickup.storeservice.domain.item.dto.FetchItemDto;
import com.justpickup.storeservice.domain.item.dto.ItemDto;
import com.justpickup.storeservice.domain.item.service.ItemService;
import com.justpickup.storeservice.domain.itemoption.dto.ItemOptionDto;
import com.justpickup.storeservice.domain.itemoption.entity.OptionType;
import com.justpickup.storeservice.global.dto.Result;
import com.justpickup.storeservice.global.entity.Yn;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owner")
public class ItemOwnerApiController {

    private final ItemService itemService;

    @GetMapping("/item")
    public ResponseEntity<Result<GetItemResponse>> getItemList(@RequestParam(required = false) Optional<String> word,
                                                               @PageableDefault Pageable pageable,
                                                               @RequestHeader(value = "user-id") String userId ){


        Page<ItemDto> itemDtoList =
                itemService.findMenuItemList(Long.parseLong(userId),
                        word.orElse(""),
                        pageable);
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
        FetchItemDto fetchItemDto = itemService.fetchItem(itemId);

        GetItemResponse getItemResponse = new GetItemResponse(fetchItemDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Result.createSuccessResult(getItemResponse));
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    static class GetItemResponse {
        private Long id;
        private String name;
        private Yn salesYn;
        private Long price;
        private Long CategoryId;
        private List<ItemOptionResponse> itemOptions;

        public GetItemResponse(FetchItemDto fetchItemDto) {
            this.id = fetchItemDto.getId();
            this.name = fetchItemDto.getName();
            this.salesYn = fetchItemDto.getSalesYn();
            this.price = fetchItemDto.getPrice();
            this.CategoryId = fetchItemDto.getCategoryDto().getId();
            this.itemOptions = fetchItemDto.getItemOptions()
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
                this.name = itemOptionDto.getName();
            }
        }
    }

    @PutMapping("/item/{itemId}")
    public ResponseEntity<Result> putItem(@RequestBody @Valid ItemRequest itemRequest,@PathVariable Long itemId){

        List<ItemOptionDto> itemOption = itemRequest.getRequiredOption().stream()
                .map(ItemRequest.ItemOptionRequest::createItemOptionDto)
                .collect(Collectors.toList());
        itemOption.addAll(itemRequest.getOtherOption().stream()
                .map(ItemRequest.ItemOptionRequest::createItemOptionDto)
                .collect(Collectors.toList()));

        itemService.putItem(itemId
                , itemRequest.getItemName()
                , itemRequest.getItemPrice()
                , itemRequest.getCategoryId()
                , itemOption);


        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(Result.success());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemRequest {
        private Long itemId;
        @NotNull
        private String itemName;
        @NotNull
        private Long itemPrice;
        @NotNull
        private Long categoryId;
        private List<ItemOptionRequest> requiredOption;
        private List<ItemOptionRequest> otherOption;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class ItemOptionRequest {
            private Long id;
            private String name;
            private OptionType optionType;

            public static ItemOptionDto createItemOptionDto(ItemOptionRequest itemOptionRequest){
                return ItemOptionDto.builder()
                        .id(itemOptionRequest.getId())
                        .name(itemOptionRequest.getName())
                        .optionType(itemOptionRequest.getOptionType())
                        .build();

            }
        }
    }

    @PostMapping("/item")
    public ResponseEntity createItem( @RequestBody @Valid ItemRequest itemRequest,
                                      @RequestHeader(value = "user-id") String userId ){


        List<ItemOptionDto> itemOption = itemRequest.getRequiredOption().stream()
                .map(ItemRequest.ItemOptionRequest::createItemOptionDto)
                .collect(Collectors.toList());
        itemOption.addAll(itemRequest.getOtherOption().stream()
                .map(ItemRequest.ItemOptionRequest::createItemOptionDto)
                .collect(Collectors.toList()));

        itemService.createItem(Long.parseLong(userId)
                , itemRequest.getItemName()
                , itemRequest.getItemPrice()
                , itemRequest.getCategoryId()
                , itemOption);


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Result.success());

    }

}
