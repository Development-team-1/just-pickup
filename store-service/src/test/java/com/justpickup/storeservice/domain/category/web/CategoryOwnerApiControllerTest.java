package com.justpickup.storeservice.domain.category.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justpickup.storeservice.config.TestConfig;
import com.justpickup.storeservice.domain.category.dto.CategoryDto;
import com.justpickup.storeservice.domain.category.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
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
        Long storeId = 1L;
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        categoryDtoList.add(CategoryDto.builder()
                .id(10L)
                .name("카테고리1")
                .order(1)
                .build());

        categoryDtoList.add(CategoryDto.builder()
                .id(11L)
                .name("카테고리2")
                .order(2)
                .build());

        BDDMockito.given(categoryService.getCategoryList(1L)).willReturn(categoryDtoList);
        //when

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
                .get("/category")
                .param("storeId",String.valueOf(storeId))
        );

        //then

        actions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].categoryId").value("10"))
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].name").value("카테고리1"))
                .andExpect(MockMvcResultMatchers.jsonPath("data[1].categoryId").value("11"))
                .andExpect(MockMvcResultMatchers.jsonPath("data[1].name").value("카테고리2"))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("get-categoryList",
                        requestParameters(
                                parameterWithName("storeId").description("매장 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("결과 코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data[*].categoryId").description("카테고리 고유 번호"),
                                fieldWithPath("data[*].name").description("카테고리 명"),
                                fieldWithPath("data[*].order").description("순서")
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
                        .put("/category")
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