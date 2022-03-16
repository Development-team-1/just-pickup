package com.justpickup.storeservice.domain.item.web;

import com.justpickup.storeservice.domain.item.dto.FetchItemDto;
import com.justpickup.storeservice.domain.item.dto.GetItemDto;
import com.justpickup.storeservice.domain.item.dto.ItemDto;
import com.justpickup.storeservice.domain.item.service.ItemService;
import com.justpickup.storeservice.domain.itemoption.dto.ItemOptionDto;
import com.justpickup.storeservice.domain.itemoption.entity.OptionType;
import com.justpickup.storeservice.global.dto.Result;
import com.justpickup.storeservice.global.entity.Yn;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class ItemCustomerApiController {

    private final ItemService itemService;

    @GetMapping("/items/{itemId}")
    public ResponseEntity getItemAndItemOptions(@PathVariable("itemId") List<Long> itemId) {
        List<GetItemDto> itemList = itemService.getItemAndItemOptions(itemId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Result.createSuccessResult(itemList));
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity fetchItem(@PathVariable("itemId") Long itemId) {
        FetchItemDto fetchItem = itemService.fetchItem(itemId);

        GetItemResponse getItemResponse = new GetItemResponse(fetchItem);
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
        private Long storeId;

        public GetItemResponse(FetchItemDto fetchItemDto) {
            this.id = fetchItemDto.getId();
            this.name = fetchItemDto.getName();
            this.salesYn = fetchItemDto.getSalesYn();
            this.price = fetchItemDto.getPrice();
            this.CategoryId = fetchItemDto.getCategoryDto().getId();
            this.itemOptions = fetchItemDto.getItemOptions()
                    .stream().map(ItemOptionResponse::new)
                    .collect(Collectors.toList());
            this.storeId = fetchItemDto.getStoreDto().getId();
        }

        @Data
        static class ItemOptionResponse{
            private Long id;

            private OptionType optionType;

            private String name;

            public ItemOptionResponse(ItemOptionDto itemOptionDto) {

                this.id = itemOptionDto.getId();
                this.optionType = itemOptionDto.getOptionType();
                this.name = itemOptionDto.getName();
            }
        }
    }


}
