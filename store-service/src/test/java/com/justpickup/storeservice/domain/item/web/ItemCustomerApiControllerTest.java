package com.justpickup.storeservice.domain.item.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justpickup.storeservice.config.TestConfig;
import com.justpickup.storeservice.domain.favoritestore.repository.FavoriteStoreRepository;
import com.justpickup.storeservice.domain.item.dto.GetItemDto;
import com.justpickup.storeservice.domain.item.dto.ItemDto;
import com.justpickup.storeservice.domain.item.service.ItemService;
import com.justpickup.storeservice.domain.itemoption.entity.OptionType;
import com.justpickup.storeservice.domain.store.repository.StoreRepository;
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

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

@WebMvcTest(ItemCustomerApiController.class)
@Import(TestConfig.class)
@AutoConfigureRestDocs(uriHost = "just-pickup.com", uriPort = 8000)
class ItemCustomerApiControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ItemService itemService;

    @MockBean
    private StoreRepository storeRepository;

    @MockBean
    private FavoriteStoreRepository favoriteStoreRepository;

    @Test
    @DisplayName("상품리스트 조회")
    void getItem() throws Exception {
        // GIVEN
        List<String> itemIds = List.of("1","2");
        List<GetItemDto> willReturnDtoList =
            List.of(
                    GetItemDto.builder()
                            .id(1L)
                            .salesYn(Yn.Y)
                            .price(1500L)
                            .name("아메리카노")
                            .itemOptions(List.of(
                                    GetItemDto.ItemOptionDto.builder()
                                            .id(1L)
                                            .name("Hot")
                                            .optionType(OptionType.REQUIRED)
                                            .build()
                                    ,GetItemDto.ItemOptionDto.builder()
                                            .id(2L)
                                            .name("add shot")
                                            .optionType(OptionType.OTHER)
                                            .build()
                    ))
                    .build(),
                    GetItemDto.builder()
                            .id(2L)
                            .salesYn(Yn.Y)
                            .price(2500L)
                            .name("카페라테")
                            .itemOptions(List.of(
                                    GetItemDto.ItemOptionDto.builder()
                                            .id(1L)
                                            .name("Hot")
                                            .optionType(OptionType.REQUIRED)
                                            .build()
                                    ,GetItemDto.ItemOptionDto.builder()
                                            .id(2L)
                                            .name("add shot")
                                            .optionType(OptionType.OTHER)
                                            .build()
                            ))
                            .build()
            );


        given(itemService.getItemAndItemOptions(any() ))
                .willReturn(willReturnDtoList);

        String param = String.join(",", itemIds);

        // WHEN
        ResultActions actions = mockMvc.perform(get("/api/customer/items/{itemIds}", param));

        // THEN
        actions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("customer-itemList-get",
                        pathParameters(
                                parameterWithName("itemIds").description("상품 고유 번호리스트")
                        ),
                        responseFields(
                                fieldWithPath("code").description("결과 코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data[*].id").description("상품 고유 번호"),
                                fieldWithPath("data[*].name").description("상품 이름"),
                                fieldWithPath("data[*].salesYn").description("화면 표시 여부 Y/N"),
                                fieldWithPath("data[*].price").description("상품 가격"),
                                fieldWithPath("data[*].itemOptions[*].id").description("아이템 옵션 고유 번호"),
                                fieldWithPath("data[*].itemOptions[*].optionType").description("옵션 타입"),
                                fieldWithPath("data[*].itemOptions[*].name").description("옵션명")
                        )
                ));
    }

}