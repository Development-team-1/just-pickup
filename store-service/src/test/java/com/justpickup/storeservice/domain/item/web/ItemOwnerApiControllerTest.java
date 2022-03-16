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
    @DisplayName("상품 리스트 조회")
    void getItemList() throws Exception {
        // GIVEN
        List<ItemDto> items = List.of(
                ItemDto.builder()
                        .id(1L)
                        .salesYn(Yn.Y)
                        .price(1500L)
                        .name("아메리카노")
                        .itemOptions(new ArrayList<>())
                        .categoryDto(new CategoryDto())
                        .build(),
                ItemDto.builder()
                        .id(2L)
                        .salesYn(Yn.Y)
                        .price(2000L)
                        .name("카페라테")
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
                                parameterWithName("word").description("아이템 명 or 카테고리 명")
                        ),
                        responseFields(
                                fieldWithPath("code").description("결과 코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data.itemList[*].id").description("상품 고유 번호"),
                                fieldWithPath("data.itemList[*].name").description("상품 이름"),
                                fieldWithPath("data.itemList[*].salesYn").description("화면 표시 여부 Y/N"),
                                fieldWithPath("data.itemList[*].price").description("상품 가격"),
                                fieldWithPath("data.itemList[*].categoryName").description("카테고리 명"),
                                fieldWithPath("data.page.startPage").description("현제 페이지"),
                                fieldWithPath("data.page.totalPage").description("토탈 페이지")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("상품 조회")
    void getItem() throws Exception {
        // GIVEN
        long itemId = 1L;
        FetchItemDto willReturnDto = FetchItemDto.builder()
                .id(1L)
                .salesYn(Yn.Y)
                .price(1500L)
                .name("아메리카노")
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
                .andExpect(jsonPath("data.name").value("아메리카노"))
                .andExpect(jsonPath("data.salesYn").value(Yn.Y.name()))
                .andExpect(jsonPath("data.price").value(1500))
                .andDo(print())
                .andDo(document("owner-item-get",
                        pathParameters(
                                parameterWithName("itemId").description("상품 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("결과 코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data.id").description("상품 고유 번호"),
                                fieldWithPath("data.name").description("상품 이름"),
                                fieldWithPath("data.salesYn").description("화면 표시 여부 Y/N"),
                                fieldWithPath("data.price").description("상품 가격"),
                                fieldWithPath("data.itemOptions").description("아이템 옵션"),
                                fieldWithPath("data.categoryId").description("카테고리 고유번호")
                        )
                ))
        ;
    }


    @Test
    @DisplayName("메뉴 등록")
    void created_item_success() throws Exception{
        //given
        String itemName = "테스트아이템";
        Long itemPrice = 3000L;
        Long categoryId = 1L;

        List<ItemOwnerApiController.ItemRequest.ItemOptionRequest> requiredOption =
                List.of(new ItemOwnerApiController.ItemRequest.ItemOptionRequest(null, "HOT",OptionType.REQUIRED));
        List<ItemOwnerApiController.ItemRequest.ItemOptionRequest> otherOption =
                List.of(new ItemOwnerApiController.ItemRequest.ItemOptionRequest(null, "샷 추가", OptionType.OTHER));


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
                                fieldWithPath("itemId").description("아이템 고유번호"),
                                fieldWithPath("itemName").description("아이템 이름"),
                                fieldWithPath("itemPrice").description("아이템 가격"),
                                fieldWithPath("categoryId").description("카테고리 고유번호"),
                                fieldWithPath("requiredOption").description("필수옵션"),
                                fieldWithPath("requiredOption[*].id").description("옵션 고유번호"),
                                fieldWithPath("requiredOption[*].name").description("옵션 이름"),
                                fieldWithPath("requiredOption[*].optionType").description("옵션 타입"),
                                fieldWithPath("otherOption").description("추가옵션"),
                                fieldWithPath("otherOption[*].id").description("옵션 고유번호"),
                                fieldWithPath("otherOption[*].name").description("옵션 이름"),
                                fieldWithPath("otherOption[*].optionType").description("옵션 타입")
                        ))
                );
    }

    @Test
    @DisplayName("메뉴 수정")
    void put_item_success() throws Exception{
        //given
        String itemName = "테스트아이템";
        Long itemPrice = 3000L;
        Long categoryId = 1L;

        List<ItemOwnerApiController.ItemRequest.ItemOptionRequest> requiredOption =
                List.of(new ItemOwnerApiController.ItemRequest.ItemOptionRequest(null, "HOT",OptionType.REQUIRED));
        List<ItemOwnerApiController.ItemRequest.ItemOptionRequest> otherOption =
                List.of(new ItemOwnerApiController.ItemRequest.ItemOptionRequest(null, "샷 추가",OptionType.OTHER));


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
                                fieldWithPath("itemId").description("아이템 고유번호"),
                                fieldWithPath("itemName").description("아이템 이름"),
                                fieldWithPath("itemPrice").description("아이템 가격"),
                                fieldWithPath("categoryId").description("카테고리 고유번호"),
                                fieldWithPath("requiredOption").description("필수옵션"),
                                fieldWithPath("requiredOption[*].id").description("옵션 고유번호"),
                                fieldWithPath("requiredOption[*].name").description("옵션 이름"),
                                fieldWithPath("requiredOption[*].optionType").description("옵션 타입"),
                                fieldWithPath("otherOption").description("추가옵션"),
                                fieldWithPath("otherOption[*].id").description("옵션 고유번호"),
                                fieldWithPath("otherOption[*].name").description("옵션 이름"),
                                fieldWithPath("otherOption[*].optionType").description("옵션 타입")
                        ))
                );
    }



}