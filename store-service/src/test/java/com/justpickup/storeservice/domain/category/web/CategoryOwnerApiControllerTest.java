package com.justpickup.storeservice.domain.category.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justpickup.storeservice.config.TestConfig;
import com.justpickup.storeservice.domain.category.dto.CategoryDto;
import com.justpickup.storeservice.domain.category.service.CategoryService;
import com.justpickup.storeservice.domain.favoritestore.repository.FavoriteStoreRepository;
import com.justpickup.storeservice.domain.item.dto.ItemDto;
import com.justpickup.storeservice.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

@WebMvcTest(CategoryOwnerApiController.class)
@Import(TestConfig.class)
@AutoConfigureRestDocs(uriHost = "127.0.0.1",uriPort = 8001)
class CategoryOwnerApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @Test
    @DisplayName("카테고리리스트_가져오기_성공")
    void getCategoryList_success() throws Exception {
        //given
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        categoryDtoList.add(CategoryDto.builder()
                .id(10L)
                .name("카페인")
                .items(List.of(new ItemDto(1L,"아메리카노",null,5000L)
                        ,new ItemDto(2L,"카페라테",null,5000L)))
                .order(1)
                .build());

        categoryDtoList.add(CategoryDto.builder()
                .id(11L)
                .name("디저트")
                .items(List.of(new ItemDto(3L,"비스킷",null,5000L)
                        ,new ItemDto(4L,"와플",null,5000L)))
                .order(2)
                .build());

        given(categoryService.getCategoriesWithItemByUserId(any())).willReturn(categoryDtoList);
        //when

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/api/owner/category")
                .header("user-id","2")
        );

        //then

        actions.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("get-categoryList",
                        requestHeaders(
                                headerWithName("user-id").description("로그인한 유저 id")
                        ),
                        responseFields(
                                fieldWithPath("code").description("결과 코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data[*].categoryId").description("카테고리 고유 번호"),
                                fieldWithPath("data[*].name").description("카테고리 명"),
                                fieldWithPath("data[*].order").description("순서"),
                                fieldWithPath("data[*].items[*].id").description("아이템 고유번호"),
                                fieldWithPath("data[*].items[*].name").description("아이템 명")
                        )
                        ));

    }

    @Test
    @DisplayName("카테고리리스트_수정_성공")
    void putCategoryList_success() throws Exception {

        //given
        Long storeId = 1L;
        List<CategoryOwnerApiController.PutCategoryRequest.Category> categoryList = new ArrayList<>();
        categoryList.add(CategoryOwnerApiController.PutCategoryRequest.Category.builder()
                .categoryId(10L)
                .name("카테고리1")
                .order(2)
                .build());

        categoryList.add(CategoryOwnerApiController.PutCategoryRequest.Category.builder()
                .categoryId(11L)
                .name("카테고리2")
                .order(1)
                .build());

        List<CategoryOwnerApiController.PutCategoryRequest.Category> deletedList = new ArrayList<>();

        deletedList.add(CategoryOwnerApiController.PutCategoryRequest.Category.builder()
                .categoryId(11L)
                .name("Non Coffee")
                .order(3)
                .build());


        CategoryOwnerApiController.PutCategoryRequest putCategoryRequest =
                CategoryOwnerApiController.PutCategoryRequest.builder()
                        .storeId(storeId)
                        .categoryList(categoryList)
                        .deletedList(deletedList)
                        .build();

        //when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/api/owner//category")
                        .content(objectMapper.writeValueAsString(putCategoryRequest) )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        //then

        actions.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("put-categoryList",
                        requestFields(
                                fieldWithPath("storeId").description("매장 고유 번호"),
                                fieldWithPath("categoryList").description("수정된 카테고리 리스트"),
                                fieldWithPath("categoryList[*].categoryId").description("카테고리 고유 번호"),
                                fieldWithPath("categoryList[*].name").description("카테고리명"),
                                fieldWithPath("categoryList[*].order").description("순서"),
                                fieldWithPath("deletedList").description("삭제된 카테고리 리스트"),
                                fieldWithPath("deletedList[*].categoryId").description("카테고리 고유 번호"),
                                fieldWithPath("deletedList[*].name").description("카테고리명"),
                                fieldWithPath("deletedList[*].order").description("순서")
                        )
                ));
    }




}