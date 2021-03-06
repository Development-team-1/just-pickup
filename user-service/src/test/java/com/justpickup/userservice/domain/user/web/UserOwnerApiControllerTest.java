package com.justpickup.userservice.domain.user.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justpickup.userservice.config.TestConfig;
import com.justpickup.userservice.domain.user.service.UserService;
import com.justpickup.userservice.global.security.SecurityConfig;
import com.justpickup.userservice.global.utils.CookieProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserOwnerApiController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)}
)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestConfig.class)
@AutoConfigureRestDocs(uriHost = "admin.just-pickup.com", uriPort = 8001)
class UserOwnerApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @SpyBean
    CookieProvider cookieProvider;

    @MockBean
    UserService userService;

    @Test
    @DisplayName("?????? ????????? ????????????")
    void postStoreOwner() throws Exception {
        // GIVEN
        UserOwnerApiController.PostStoreOwnerRequest requestBody
                = UserOwnerApiController.PostStoreOwnerRequest.builder()
                        .email("test@gmail.com")
                        .password("1234")
                        .name("????????? ??????")
                        .phoneNumber("010-1234-5678")
                        .businessNumber("03124")
                        .storeName("????????? ??????")
                        .storePhoneNumber("010-1234-5678")
                        .address("?????? ????????? ????????? 1????????? 627")
                        .zipcode("28562")
                        .latitude(36.6375346629654)
                        .longitude(127.459726819858)
                        .build();
        String content = objectMapper.writeValueAsString(requestBody);
        // THEN
        ResultActions actions = mockMvc.perform(post("/api/owner/store-owner")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content)
        );
        // WHEN
        actions.andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("api-owner-post-store-owner",
                        requestFields(
                                fieldWithPath("email").description("?????????"),
                                fieldWithPath("password").description("????????????"),
                                fieldWithPath("name").description("??????"),
                                fieldWithPath("phoneNumber").description("????????? ??????"),
                                fieldWithPath("businessNumber").description("???????????????"),
                                fieldWithPath("storeName").description("?????? ??????"),
                                fieldWithPath("storePhoneNumber").description("?????? ??????"),
                                fieldWithPath("address").description("?????? ??????"),
                                fieldWithPath("zipcode").description("?????? ????????????"),
                                fieldWithPath("latitude").description("??????"),
                                fieldWithPath("longitude").description("??????")
                        )
                        )
                )
        ;
    }
}