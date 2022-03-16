package com.justpickup.storeservice.domain.item.web;

import com.justpickup.storeservice.domain.item.dto.ItemDto;
import com.justpickup.storeservice.domain.item.dto.ItemsDto;
import com.justpickup.storeservice.domain.item.service.ItemService;
import com.justpickup.storeservice.global.dto.Result;
import com.justpickup.storeservice.global.entity.Yn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;


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

        public GetItemResponse(ItemDto itemDto) {
            this.id = itemDto.getId();
            this.name = itemDto.getName();
            this.salesYn = itemDto.getSalesYn();
            this.price = itemDto.getPrice();
        }
    }

    @GetMapping("/items/{itemIds}")
    public ResponseEntity<Result> getItems(@PathVariable List<Long> itemIds) {
        List<ItemsDto> items = itemService.findItems(itemIds);

        List<GetItemsResponse> responses = items.stream()
                .map(GetItemsResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(Result.createSuccessResult(responses));
    }

    @Data @NoArgsConstructor
    static class GetItemsResponse {
        private Long id;
        private String name;

        public GetItemsResponse(ItemsDto itemsDto) {
            this.id = itemsDto.getItemId();
            this.name = itemsDto.getItemName();
        }
    }

}
