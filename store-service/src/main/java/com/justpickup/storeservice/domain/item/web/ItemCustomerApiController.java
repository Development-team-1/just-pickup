package com.justpickup.storeservice.domain.item.web;

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
@RequestMapping("/api/customer")
public class ItemCustomerApiController {

    private final ItemService itemService;


    @GetMapping("/item/{itemId}")
    public ResponseEntity getItem(@PathVariable("itemId") Long itemId) {
        ItemDto itemByItemId = itemService.findFullItemByItemId(itemId);

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
        private Long CategoryId;
        private List<ItemOptionResponse> itemOptions;

        public GetItemResponse(ItemDto itemDto) {
            this.id = itemDto.getId();
            this.name = itemDto.getName();
            this.salesYn = itemDto.getSalesYn();
            this.price = itemDto.getPrice();
            this.CategoryId = itemDto.getCategoryDto().getId();
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
