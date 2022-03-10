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
    @DisplayName("즐겨찾는 매장")
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
                                headerWithName("user-id").description("로그인한 유저 id")
                        ),
                        requestParameters(
                                parameterWithName("latitude").description("고객의 위도 [필수]"),
                                parameterWithName("longitude").description("고객의 경도 [필수]")
                        ),
                        responseFields(
                                fieldWithPath("code").description("결과 코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data[*].id").description("매장 고유번호"),
                                fieldWithPath("data[*].name").description("매장 이름"),
                                fieldWithPath("data[*].distance").description("고객과의 거리차이 m/km"),
                                fieldWithPath("data[*].favoriteCounts").description("즐겨찾기 회수")
                        )
                ));
    }


    private List<SearchStoreResult> getWillReturnSearchStore(){
        SearchStoreResult result_1 = new SearchStoreResult(1L, "이디야커피 마포오벨리스크점", 145.11980562222007, 5L);
        SearchStoreResult result_2 = new SearchStoreResult(2L, "만랩커피 마포점", 150.97181089895466, 5L);
        SearchStoreResult result_3 = new SearchStoreResult(3L, "커피온리 마포역점", 341.25696860337655, 5L);

        return List.of(result_1,result_2,result_3);
    }
  
    @Test
    @DisplayName("[API] [GET] 매장 검색 페이지 조회")
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
                                parameterWithName("latitude").description("고객의 위도 [필수]"),
                                parameterWithName("longitude").description("고객의 경도 [필수]"),
                                parameterWithName("page").description("검색할 페이지 [Optional, default: 0]"),
                                parameterWithName("size").description("검색할 페이지 사이즈 [Optional, default: 2]")
                        ),
                        responseFields(
                                fieldWithPath("code").description("결과 코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data.stores[*].id").description("매장 고유번호"),
                                fieldWithPath("data.stores[*].name").description("매장 이름"),
                                fieldWithPath("data.stores[*].distance").description("고객과의 거리차이 m/km"),
                                fieldWithPath("data.stores[*].favoriteCounts").description("매장 즐겨찾기 수"),
                                fieldWithPath("data.hasNext").description("더보기 버튼 유무")
                        )
                ))
        ;
    }

    private SliceImpl<SearchStoreResult> getWillReturnSearchStore(Pageable pageable) {
        SearchStoreResult result_1 = new SearchStoreResult(1L, "이디야커피 마포오벨리스크점", 145.11980562222007, 10L);
        SearchStoreResult result_2 = new SearchStoreResult(2L, "만랩커피 마포점", 150.97181089895466, 5L);
        return new SliceImpl<>(List.of(result_1, result_2), pageable,true);
    }           

}