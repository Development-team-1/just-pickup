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
    @DisplayName("점주 서비스 회원가입")
    void postStoreOwner() throws Exception {
        // GIVEN
        UserOwnerApiController.PostStoreOwnerRequest requestBody
                = UserOwnerApiController.PostStoreOwnerRequest.builder()
                        .email("test@gmail.com")
                        .password("1234")
                        .name("테스트 이름")
                        .phoneNumber("010-1234-5678")
                        .businessNumber("03124")
                        .storeName("테스트 매장")
                        .storePhoneNumber("010-1234-5678")
                        .address("충북 청주시 서원구 1순환로 627")
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
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("phoneNumber").description("핸드폰 번호"),
                                fieldWithPath("businessNumber").description("사업자번호"),
                                fieldWithPath("storeName").description("매장 이름"),
                                fieldWithPath("storePhoneNumber").description("매장 번호"),
                                fieldWithPath("address").description("매장 주소"),
                                fieldWithPath("zipcode").description("매장 우편번호"),
                                fieldWithPath("latitude").description("위도"),
                                fieldWithPath("longitude").description("경도")
                        )
                        )
                )
        ;
    }
}