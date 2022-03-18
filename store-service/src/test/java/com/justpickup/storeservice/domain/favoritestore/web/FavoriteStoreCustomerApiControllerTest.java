package com.justpickup.storeservice.domain.favoritestore.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justpickup.storeservice.config.TestConfig;
import com.justpickup.storeservice.domain.favoritestore.dto.GetFavoriteStoreByStoreIdDto;
import com.justpickup.storeservice.domain.favoritestore.service.FavoriteStoreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FavoriteStoreCustomerApiController.class)
@Import(TestConfig.class)
@AutoConfigureRestDocs(uriHost = "just-pickup.com", uriPort = 8000)
class FavoriteStoreCustomerApiControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FavoriteStoreService favoriteStoreService;


    @Test
    @DisplayName("Get_즐겨찾는 매장")
    void getFavoriteStoreByStoreId() throws Exception {
        //given
        Long userId=1L;
        Long storeId=1L;

        BDDMockito.given(favoriteStoreService
                        .getFavoriteStoreByStoreId(userId,storeId))
                .willReturn(
                        new GetFavoriteStoreByStoreIdDto(userId,storeId,true));


        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/customer/favoriteStore/{storeId}",storeId)
                .header("user-id", userId));

        //then

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-favoritestore-by-storeid",
                        requestHeaders(
                                headerWithName("user-id").description("로그인한 유저 id")
                        ),
                        pathParameters(
                                parameterWithName("storeId").description("매장 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("결과 코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data.userId").description("유저 고유번호"),
                                fieldWithPath("data.storeId").description("매장 고유 번호"),
                                fieldWithPath("data.exist").description("즐겨찾기 매장 존재여부")
                        ))
                );
    }

    @Test
    @DisplayName("즐겨찾는 매장 추가 or 제거")
    void patchFavoriteStore() throws Exception {
        //given
        Long userId=1L;
        Long storeId=1L;

        //when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/customer/favoriteStore/{storeId}",storeId)
                        .header("user-id", userId));

        //then

        resultActions.andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("patch-FavoriteStore",
                        requestHeaders(
                                headerWithName("user-id").description("로그인한 유저 id")
                        ),
                        pathParameters(
                                parameterWithName("storeId").description("매장 고유 번호")
                        )
                    )
                );
    }

}