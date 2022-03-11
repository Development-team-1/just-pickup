package com.justpickup.storeservice.domain.store.web;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
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
}