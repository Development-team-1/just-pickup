package com.justpickup.storeservice.domain.item.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justpickup.storeservice.config.TestConfig;
import com.justpickup.storeservice.domain.category.dto.CategoryDto;
import com.justpickup.storeservice.domain.item.dto.FetchItemDto;
import com.justpickup.storeservice.domain.item.dto.ItemDto;
import com.justpickup.storeservice.domain.item.service.ItemService;
import com.justpickup.storeservice.domain.itemoption.entity.OptionType;
import com.justpickup.storeservice.global.dto.Code;
import com.justpickup.storeservice.global.entity.Yn;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemOwnerApiController.class)
@Import(TestConfig.class)
@AutoConfigureRestDocs(uriHost = "127.0.0.1", uriPort = 8001)
class ItemOwnerApiControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ItemService itemService;

    @Test
    @DisplayName("?????? ????????? ??????")
    void getItemList() throws Exception {
        // GIVEN
        List<ItemDto> items = List.of(
                ItemDto.builder()
                        .id(1L)
                        .salesYn(Yn.Y)
                        .price(1500L)
                        .name("???????????????")
                        .itemOptions(new ArrayList<>())
                        .categoryDto(new CategoryDto())
                        .build(),
                ItemDto.builder()
                        .id(2L)
                        .salesYn(Yn.Y)
                        .price(2000L)
                        .name("????????????")
                        .itemOptions(new ArrayList<>())
                        .categoryDto(new CategoryDto())
                        .build()
                );

        Page<ItemDto> page = PageableExecutionUtils.getPage(items, Pageable.ofSize(10), () -> 1);

        given(itemService.findMenuItemList(eq(1L),eq(""),any()))
                .willReturn(page);

        // WHEN
        ResultActions actions = mockMvc.perform(
                get("/api/owner/item/")
                        .header("user-id","1")
                        .param("word","")
        );

        // THEN
        actions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("owner-itemList-get",
                        requestParameters(
                                parameterWithName("word").description("????????? ??? or ???????????? ???")
                        ),
                        responseFields(
                                fieldWithPath("code").description("?????? ?????? SUCCESS/ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("data.itemList[*].id").description("?????? ?????? ??????"),
                                fieldWithPath("data.itemList[*].name").description("?????? ??????"),
                                fieldWithPath("data.itemList[*].salesYn").description("?????? ?????? ?????? Y/N"),
                                fieldWithPath("data.itemList[*].price").description("?????? ??????"),
                                fieldWithPath("data.itemList[*].categoryName").description("???????????? ???"),
                                fieldWithPath("data.page.startPage").description("?????? ?????????"),
                                fieldWithPath("data.page.totalPage").description("?????? ?????????")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("?????? ??????")
    void getItem() throws Exception {
        // GIVEN
        long itemId = 1L;
        FetchItemDto willReturnDto = FetchItemDto.builder()
                .id(1L)
                .salesYn(Yn.Y)
                .price(1500L)
                .name("???????????????")
                .itemOptions(new ArrayList<>())
                .categoryDto(new CategoryDto())
                .build();
        given(itemService.fetchItem(itemId))
                .willReturn(willReturnDto);

        // WHEN
        ResultActions actions = mockMvc.perform(
                get("/api/owner/item/{itemId}", itemId)
        );

        // THEN
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("message").value(""))
                .andExpect(jsonPath("data.id").value(itemId))
                .andExpect(jsonPath("data.name").value("???????????????"))
                .andExpect(jsonPath("data.salesYn").value(Yn.Y.name()))
                .andExpect(jsonPath("data.price").value(1500))
                .andDo(print())
                .andDo(document("owner-item-get",
                        pathParameters(
                                parameterWithName("itemId").description("?????? ?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("code").description("?????? ?????? SUCCESS/ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("data.id").description("?????? ?????? ??????"),
                                fieldWithPath("data.name").description("?????? ??????"),
                                fieldWithPath("data.salesYn").description("?????? ?????? ?????? Y/N"),
                                fieldWithPath("data.price").description("?????? ??????"),
                                fieldWithPath("data.itemOptions").description("????????? ??????"),
                                fieldWithPath("data.categoryId").description("???????????? ????????????")
                        )
                ))
        ;
    }


    @Test
    @DisplayName("?????? ??????")
    void created_item_success() throws Exception{
        //given
        String itemName = "??????????????????";
        Long itemPrice = 3000L;
        Long categoryId = 1L;

        List<ItemOwnerApiController.ItemRequest.ItemOptionRequest> requiredOption =
                List.of(new ItemOwnerApiController.ItemRequest.ItemOptionRequest(null, "HOT",OptionType.REQUIRED));
        List<ItemOwnerApiController.ItemRequest.ItemOptionRequest> otherOption =
                List.of(new ItemOwnerApiController.ItemRequest.ItemOptionRequest(null, "??? ??????", OptionType.OTHER));


        ItemOwnerApiController.ItemRequest itemRequest =
                new ItemOwnerApiController.ItemRequest(null,itemName,itemPrice,categoryId,requiredOption,otherOption);

        String body = objectMapper.writeValueAsString(itemRequest);

        //when
        ResultActions perform = mockMvc.perform(post("/api/owner/item")
                .header("user-id","2")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        ResultActions resultActions = perform.andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("owner-create-item",
                        requestFields(
                                fieldWithPath("itemId").description("????????? ????????????"),
                                fieldWithPath("itemName").description("????????? ??????"),
                                fieldWithPath("itemPrice").description("????????? ??????"),
                                fieldWithPath("categoryId").description("???????????? ????????????"),
                                fieldWithPath("requiredOption").description("????????????"),
                                fieldWithPath("requiredOption[*].id").description("?????? ????????????"),
                                fieldWithPath("requiredOption[*].name").description("?????? ??????"),
                                fieldWithPath("requiredOption[*].optionType").description("?????? ??????"),
                                fieldWithPath("otherOption").description("????????????"),
                                fieldWithPath("otherOption[*].id").description("?????? ????????????"),
                                fieldWithPath("otherOption[*].name").description("?????? ??????"),
                                fieldWithPath("otherOption[*].optionType").description("?????? ??????")
                        ))
                );
    }

    @Test
    @DisplayName("?????? ??????")
    void put_item_success() throws Exception{
        //given
        String itemName = "??????????????????";
        Long itemPrice = 3000L;
        Long categoryId = 1L;

        List<ItemOwnerApiController.ItemRequest.ItemOptionRequest> requiredOption =
                List.of(new ItemOwnerApiController.ItemRequest.ItemOptionRequest(null, "HOT",OptionType.REQUIRED));
        List<ItemOwnerApiController.ItemRequest.ItemOptionRequest> otherOption =
                List.of(new ItemOwnerApiController.ItemRequest.ItemOptionRequest(null, "??? ??????",OptionType.OTHER));


        ItemOwnerApiController.ItemRequest itemRequest =
                new ItemOwnerApiController.ItemRequest(1L,itemName,itemPrice,categoryId,requiredOption,otherOption);

        String body = objectMapper.writeValueAsString(itemRequest);

        //when
        ResultActions perform = mockMvc.perform(put("/api/owner/item/"+itemRequest.getItemId())
                .header("user-id","2")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );
        //then
        ResultActions resultActions = perform.andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("owner-put-item",
                        requestFields(
                                fieldWithPath("itemId").description("????????? ????????????"),
                                fieldWithPath("itemName").description("????????? ??????"),
                                fieldWithPath("itemPrice").description("????????? ??????"),
                                fieldWithPath("categoryId").description("???????????? ????????????"),
                                fieldWithPath("requiredOption").description("????????????"),
                                fieldWithPath("requiredOption[*].id").description("?????? ????????????"),
                                fieldWithPath("requiredOption[*].name").description("?????? ??????"),
                                fieldWithPath("requiredOption[*].optionType").description("?????? ??????"),
                                fieldWithPath("otherOption").description("????????????"),
                                fieldWithPath("otherOption[*].id").description("?????? ????????????"),
                                fieldWithPath("otherOption[*].name").description("?????? ??????"),
                                fieldWithPath("otherOption[*].optionType").description("?????? ??????")
                        ))
                );
    }



}