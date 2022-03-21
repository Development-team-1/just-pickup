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
    @DisplayName("[API] [GET] 회원 고유번호로 매장 정보 찾기")
    void getStoreByUserId() throws Exception {
        // GIVEN
        String userHeader = "1";
        Long userId = Long.valueOf(userHeader);
        StoreByUserIdDto willReturnDto = StoreByUserIdDto.builder().id(10L).name("한강커피").build();
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
                                headerWithName("user-id").description("로그인한 유저 id")
                        ),
                        responseFields(
                                fieldWithPath("code").description("결과 코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data.id").description("매장 고유번호"),
                                fieldWithPath("data.name").description("매장 이름")
                        )
                        ))
        ;
    }

    @Test
    @DisplayName("[API] 점주 등록")
    void postStore() throws Exception {
        // GIVEN
        StoreOwnerApiController.PostStoreRequest request = StoreOwnerApiController.PostStoreRequest.builder()
                .name("점주 이름")
                .phoneNumber("010-1234-5678")
                .address("서울특별시 마포구 용강동 123-1길")
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
                                headerWithName("user-id").description("JWT 유저 고유 번호")
                        ),
                        requestFields(
                                fieldWithPath("name").description("매징 이름"),
                                fieldWithPath("phoneNumber").description("매장 번호"),
                                fieldWithPath("address").description("매장 주소"),
                                fieldWithPath("zipcode").description("매장 우편번호"),
                                fieldWithPath("latitude").description("위도"),
                                fieldWithPath("longitude").description("경도")
                        )
                ))
        ;
    }
}