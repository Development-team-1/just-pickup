package com.justpickup.storeservice.domain.item.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justpickup.storeservice.config.TestConfig;
import com.justpickup.storeservice.domain.item.dto.ItemDto;
import com.justpickup.storeservice.domain.item.dto.ItemsDto;
import com.justpickup.storeservice.domain.item.exception.NotExistItemException;
import com.justpickup.storeservice.domain.item.service.ItemService;
import com.justpickup.storeservice.global.dto.Code;
import com.justpickup.storeservice.global.entity.Yn;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@Import(TestConfig.class)
@AutoConfigureRestDocs(uriHost = "127.0.0.1", uriPort = 8001)
class ItemControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ItemService itemService;

    @Test
    @DisplayName("?????? ??????")
    void getItem() throws Exception {
        // GIVEN
        long itemId = 1L;
        ItemDto willReturnDto = ItemDto.builder()
                .id(1L)
                .salesYn(Yn.Y)
                .price(1500L)
                .name("???????????????")
                .build();
        given(itemService.findItemByItemId(itemId))
                .willReturn(willReturnDto);

        // WHEN
        ResultActions actions = mockMvc.perform(get("/item/{itemId}", itemId));

        // THEN
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("message").value(""))
                .andExpect(jsonPath("data.id").value(itemId))
                .andExpect(jsonPath("data.name").value("???????????????"))
                .andExpect(jsonPath("data.salesYn").value(Yn.Y.name()))
                .andExpect(jsonPath("data.price").value(1500))
                .andDo(print())
                .andDo(document("item-get",
                        pathParameters(
                                parameterWithName("itemId").description("?????? ?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("code").description("?????? ?????? SUCCESS/ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("data.id").description("?????? ?????? ??????"),
                                fieldWithPath("data.name").description("?????? ??????"),
                                fieldWithPath("data.salesYn").description("?????? ?????? ?????? Y/N"),
                                fieldWithPath("data.price").description("?????? ??????")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ?????? - ???????????? ?????? ??????")
    void getItemNotExistItemException() throws Exception {
        // GIVEN
        long notExistItemId = 9999L;
        String message = "???????????? ?????? ???????????????.";
        given(itemService.findItemByItemId(notExistItemId))
                .willThrow(new NotExistItemException(message));

        // THEN
        ResultActions actions = mockMvc.perform(get("/item/{itemId}", notExistItemId));

        // WHEN
        actions.andExpect(status().isConflict())
                .andExpect(jsonPath("code").value(Code.ERROR.name()))
                .andExpect(jsonPath("message").value(message))
                .andExpect(jsonPath("data").isEmpty())
                .andDo(print())
                .andDo(document("item-get-notExistItemException",
                        pathParameters(
                                parameterWithName("itemId").description("?????? ?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("code").description("?????? ?????? SUCCESS/ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("data").description("?????????")
                        )
                        ))
        ;
    }

    @Test
    @DisplayName("[GET] ????????? ????????? ??????")
    void getItems() throws Exception {
        // GIVEN
        List<Long> itemIds = List.of(1L, 2L, 3L);

        given(itemService.findItems(itemIds))
                .willReturn(itemsWillReturnDto(itemIds));

        String itemIdsParam = itemIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        // THEN
        ResultActions actions = mockMvc.perform(get("/items/{itemIds}", itemIdsParam));
        // WHEN
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("message").value(""))
                .andDo(print())
                .andDo(document("items-get",
                        pathParameters(
                                parameterWithName("itemIds").description("?????? ?????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("code").description("?????? ?????? SUCCESS/ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("data[*].id").description("?????? ?????? ??????"),
                                fieldWithPath("data[*].name").description("?????? ??????")
                        )
                        ))
        ;
    }

    private List<ItemsDto> itemsWillReturnDto(List<Long> itemIds) {
        List<ItemsDto> items = new ArrayList<>();

        for (Long itemId : itemIds) {
            ItemsDto itemsDto = ItemsDto.builder()
                    .itemId(itemId)
                    .itemName("????????? ??????" + itemId)
                    .build();
            items.add(itemsDto);
        }

        return items;
    }

}