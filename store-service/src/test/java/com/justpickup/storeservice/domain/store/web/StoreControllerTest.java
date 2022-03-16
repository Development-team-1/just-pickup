package com.justpickup.storeservice.domain.store.web;

import com.justpickup.storeservice.config.TestConfig;
import com.justpickup.storeservice.domain.store.dto.StoreDto;
import com.justpickup.storeservice.domain.store.service.StoreService;
import com.justpickup.storeservice.global.dto.Code;
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

@WebMvcTest(StoreController.class)
@Import(TestConfig.class)
@AutoConfigureRestDocs(uriHost = "just-pickup.com", uriPort = 8000)
class StoreControllerTest {

    private final String url = "/store";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StoreService storeService;

    @Test
    @DisplayName("[store] 매장 정보 가져오기")
    void getStore() throws Exception {
        //given
        String storeId = "1";
        given(storeService.findStoreById(1L)).willReturn(getWillReturnStore());

        //when
        ResultActions actions = mockMvc.perform(get("/store/{storeId}", storeId));

        //then
        actions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("store-get",
                        pathParameters(
                            parameterWithName("storeId").description("매장 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("결과 코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data.id").description("매장 고유번호"),
                                fieldWithPath("data.name").description("매장 이름"),
                                fieldWithPath("data.phoneNumber").description("매장 번호")
                        )
                ));
    }

    private StoreDto getWillReturnStore() {
        return StoreDto.builder().id(1L).name("이디야커피 대림역점").phoneNumber("010-1234-5678").build();
    }

    @Test
    @DisplayName("[GET] 매장 정보들 가져오기")
    void getStores() throws Exception {
        // GIVEN
        List<Long> storeIds = List.of(1L, 2L, 3L);

        given(storeService.findStoreAllById(storeIds))
                .willReturn(storesWillReturnDto(storeIds));

        String storeIdsParam = storeIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        // THEN
        ResultActions actions = mockMvc.perform(get("/stores/{storeIds}", storeIdsParam));
        // WHEN
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("message").value(""))
                .andDo(print())
                .andDo(document("stores-get",
                        pathParameters(
                                parameterWithName("storeIds").description("매장 고유 번호들")
                        ),
                        responseFields(
                                fieldWithPath("code").description("결과 코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data[*].id").description("매장 고유 번호"),
                                fieldWithPath("data[*].name").description("매장 이름"),
                                fieldWithPath("data[*].phoneNumber").description("매장 휴대폰 번호")
                        )
                ))
        ;
    }

    private List<StoreDto> storesWillReturnDto(List<Long> storeIds) {
        List<StoreDto> stores = new ArrayList<>();
        for (Long storeId : storeIds) {
            StoreDto storeDto = StoreDto.builder()
                    .id(storeId)
                    .name("매장 이름" + storeId)
                    .phoneNumber("010-1234-5678")
                    .build();
            stores.add(storeDto);
        }
        return stores;
    }
}