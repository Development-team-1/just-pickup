package com.justpickup.storeservice.domain.store.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justpickup.storeservice.config.TestConfig;
import com.justpickup.storeservice.domain.favoritestore.repository.FavoriteStoreRepository;
import com.justpickup.storeservice.domain.store.dto.SearchStoreCondition;
import com.justpickup.storeservice.domain.store.dto.SearchStoreResult;
import com.justpickup.storeservice.domain.store.repository.StoreRepository;
import com.justpickup.storeservice.domain.store.service.StoreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StoreCustomerApiController.class)
@Import(TestConfig.class)
@AutoConfigureRestDocs(uriHost = "just-pickup.com", uriPort = 8000)
class StoreCustomerApiControllerTest {

    private final String url = "/api/customer/store";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StoreService storeService;

    @MockBean
    StoreRepository storeRepository;

    @MockBean
    FavoriteStoreRepository favoriteStoreRepository;

    @Test
    @DisplayName("???????????? ??????")
    void getFavoriteStore() throws Exception {
        //given

        double latitude = 37.5403912;
        double longitude = 126.9438922;
        SearchStoreCondition condition = new SearchStoreCondition(latitude, longitude, null);
        given(storeService.findFavoriteStore(any(SearchStoreCondition.class),eq(2L)))
                .willReturn(getWillReturnSearchStore());


        //when
        String s = new ObjectMapper()
                .writeValueAsString(condition);

        ResultActions resultActions = mockMvc.perform(get("/api/customer/store/favorite")
                .param("latitude",String.valueOf(condition.getLatitude()))
                .param("longitude",String.valueOf(condition.getLongitude()))
                .header("user-id","2")
        );

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("favoriteStore-get",
                        requestHeaders(
                                headerWithName("user-id").description("???????????? ?????? id")
                        ),
                        requestParameters(
                                parameterWithName("latitude").description("????????? ?????? [??????]"),
                                parameterWithName("longitude").description("????????? ?????? [??????]")
                        ),
                        responseFields(
                                fieldWithPath("code").description("?????? ?????? SUCCESS/ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("data[*].id").description("?????? ????????????"),
                                fieldWithPath("data[*].name").description("?????? ??????"),
                                fieldWithPath("data[*].distance").description("???????????? ???????????? m/km"),
                                fieldWithPath("data[*].favoriteCounts").description("???????????? ??????")
                        )
                ));
    }


    private List<SearchStoreResult> getWillReturnSearchStore(){
        SearchStoreResult result_1 = new SearchStoreResult(1L, "??????????????? ????????????????????????", 145.11980562222007, 5L);
        SearchStoreResult result_2 = new SearchStoreResult(2L, "???????????? ?????????", 150.97181089895466, 5L);
        SearchStoreResult result_3 = new SearchStoreResult(3L, "???????????? ????????????", 341.25696860337655, 5L);

        return List.of(result_1,result_2,result_3);
    }
  
    @Test
    @DisplayName("[API] [GET] ?????? ?????? ????????? ??????")
    void getSearchStore() throws Exception {
        // GIVEN
        double latitude = 37.5403912;
        double longitude = 126.9438922;
        SearchStoreCondition condition = new SearchStoreCondition(latitude, longitude, null);
        PageRequest pageable = PageRequest.of(0, 2);

        given(storeService.findSearchStoreScroll(any(SearchStoreCondition.class), any(Pageable.class)))
                .willReturn(getWillReturnSearchStore(pageable));

        // WHEN
        ResultActions actions = mockMvc.perform(get(url + "/search")
                .param("latitude", String.valueOf(condition.getLatitude()))
                .param("longitude", String.valueOf(condition.getLongitude()))
                .param("page", String.valueOf(pageable.getPageNumber()))
                .param("size", String.valueOf(pageable.getPageSize()))
        );

        // THEN
        actions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("api-customer-store-search",
                        requestParameters(
                                parameterWithName("latitude").description("????????? ?????? [??????]"),
                                parameterWithName("longitude").description("????????? ?????? [??????]"),
                                parameterWithName("page").description("????????? ????????? [Optional, default: 0]"),
                                parameterWithName("size").description("????????? ????????? ????????? [Optional, default: 2]")
                        ),
                        responseFields(
                                fieldWithPath("code").description("?????? ?????? SUCCESS/ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("data.stores[*].id").description("?????? ????????????"),
                                fieldWithPath("data.stores[*].name").description("?????? ??????"),
                                fieldWithPath("data.stores[*].distance").description("???????????? ???????????? m/km"),
                                fieldWithPath("data.stores[*].favoriteCounts").description("?????? ???????????? ???"),
                                fieldWithPath("data.hasNext").description("????????? ?????? ??????")
                        )
                ))
        ;
    }

    private SliceImpl<SearchStoreResult> getWillReturnSearchStore(Pageable pageable) {
        SearchStoreResult result_1 = new SearchStoreResult(1L, "??????????????? ????????????????????????", 145.11980562222007, 10L);
        SearchStoreResult result_2 = new SearchStoreResult(2L, "???????????? ?????????", 150.97181089895466, 5L);
        return new SliceImpl<>(List.of(result_1, result_2), pageable,true);
    }           

}