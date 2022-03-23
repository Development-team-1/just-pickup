package com.justpickup.orderservice.global.client.store;

import com.justpickup.orderservice.global.entity.Yn;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetItemResponse {
    private Long id;

    private String name;

    private Yn salesYn;

    private Long price;

    private List<ItemOptionDto> itemOptions;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemOptionDto{
        private Long id;

        private OptionType optionType;

        private String name;

    }
}
