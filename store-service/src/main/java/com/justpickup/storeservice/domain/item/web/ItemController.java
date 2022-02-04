package com.justpickup.storeservice.domain.item.web;

import com.justpickup.storeservice.domain.item.dto.ItemDto;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{itemId}")
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
}
