package com.justpickup.storeservice.domain.item.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justpickup.storeservice.config.TestConfig;
import com.justpickup.storeservice.domain.item.dto.ItemDto;
import com.justpickup.storeservice.domain.item.exception.NotExistItemException;
import com.justpickup.storeservice.domain.item.service.ItemService;
import com.justpickup.storeservice.domain.itemoption.dto.ItemOptionDto;
import com.justpickup.storeservice.domain.itemoption.entity.ItemOption;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.validation.constraints.NotNull;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
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
    @DisplayName("상품 조회")
    void getItem() throws Exception {
        // GIVEN
        long itemId = 1L;
        ItemDto willReturnDto = ItemDto.builder()
                .id(1L)
                .salesYn(Yn.Y)
                .price(1500L)
                .name("아메리카노")
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
                .andExpect(jsonPath("data.name").value("아메리카노"))
                .andExpect(jsonPath("data.salesYn").value(Yn.Y.name()))
                .andExpect(jsonPath("data.price").value(1500))
                .andDo(print())
                .andDo(document("item-get",
                        pathParameters(
                                parameterWithName("itemId").description("상품 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("결과 코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data.id").description("상품 고유 번호"),
                                fieldWithPath("data.name").description("상품 이름"),
                                fieldWithPath("data.salesYn").description("화면 표시 여부 Y/N"),
                                fieldWithPath("data.price").description("상품 가격")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("상품 조회 - 존재하지 않는 상품")
    void getItemNotExistItemException() throws Exception {
        // GIVEN
        long notExistItemId = 9999L;
        String message = "존재하지 않는 상품입니다.";
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
                                parameterWithName("itemId").description("상품 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("결과 코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data").description("데이터")
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

        List<ItemController.ItemRequest.ItemOptionRequest> requiredOption = List.of(new ItemController.ItemRequest.ItemOptionRequest(null, "HOT",OptionType.REQUIRED,null));
        List<ItemController.ItemRequest.ItemOptionRequest> otherOption = List.of(new ItemController.ItemRequest.ItemOptionRequest(null, "샷 추가",OptionType.OTHER,null));


        ItemController.ItemRequest itemRequest = new ItemController.ItemRequest(null,itemName,itemPrice,categoryId,requiredOption,otherOption);

        String body = objectMapper.writeValueAsString(itemRequest);

        //when
        ResultActions perform = mockMvc.perform(post("/item")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        ResultActions resultActions = perform.andExpect(status().isCreated())
                .andDo(print());

    }


}