package com.justpickup.storeservice.domain.store.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justpickup.storeservice.config.TestConfig;
import com.justpickup.storeservice.domain.store.dto.StoreByUserIdDto;
import com.justpickup.storeservice.domain.store.service.StoreService;
import com.justpickup.storeservice.global.dto.Code;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StoreOwnerApiController.class)
@Import(TestConfig.class)
@AutoConfigureRestDocs(uriHost = "just-pickup.com", uriPort = 8000)
class StoreOwnerApiControllerTest {

    private final String url = "/api";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    StoreService storeService;

    @Test
    @DisplayName("[API] [GET] ?????? ??????????????? ?????? ?????? ??????")
    void getStoreByUserId() throws Exception {
        // GIVEN
        String userHeader = "1";
        Long userId = Long.valueOf(userHeader);
        StoreByUserIdDto willReturnDto = StoreByUserIdDto.builder().id(10L).name("????????????").build();
        given(storeService.findStoreByUserId(userId)).willReturn(willReturnDto);

        // THEN
        ResultActions actions = mockMvc.perform(get(url + "/owner/store")
                .header("user-id", userHeader)
        );

        // WHEN
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("data").exists())
                .andDo(print())
                .andDo(document("api-get-store-byUserId",
                        requestHeaders(
                                headerWithName("user-id").description("???????????? ?????? id")
                        ),
                        responseFields(
                                fieldWithPath("code").description("?????? ?????? SUCCESS/ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("data.id").description("?????? ????????????"),
                                fieldWithPath("data.name").description("?????? ??????")
                        )
                        ))
        ;
    }

    @Test
    @DisplayName("[API] ?????? ??????")
    void postStore() throws Exception {
        // GIVEN
        StoreOwnerApiController.PostStoreRequest request = StoreOwnerApiController.PostStoreRequest.builder()
                .name("?????? ??????")
                .phoneNumber("010-1234-5678")
                .address("??????????????? ????????? ????????? 123-1???")
                .zipcode("129845")
                .latitude(30.90199982)
                .longitude(112.1298347)
                .build();

        Long userId = 1L;
        String content = objectMapper.writeValueAsString(request);

        // THEN
        ResultActions actions = mockMvc.perform(post(url + "/owner/store")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("user-id", String.valueOf(userId))
        );

        // WHEN
        actions.andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("api-post-store",
                        requestHeaders(
                                headerWithName("user-id").description("JWT ?????? ?????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("name").description("?????? ??????"),
                                fieldWithPath("phoneNumber").description("?????? ??????"),
                                fieldWithPath("address").description("?????? ??????"),
                                fieldWithPath("zipcode").description("?????? ????????????"),
                                fieldWithPath("latitude").description("??????"),
                                fieldWithPath("longitude").description("??????")
                        )
                ))
        ;
    }
}