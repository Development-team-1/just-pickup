package com.justpickup.userservice.domain.user.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justpickup.userservice.config.TestConfig;
import com.justpickup.userservice.domain.user.dto.CustomerDto;
import com.justpickup.userservice.domain.user.exception.NotExistUserException;
import com.justpickup.userservice.domain.user.service.UserService;
import com.justpickup.userservice.global.dto.Code;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@Import(TestConfig.class)
@AutoConfigureRestDocs(uriHost = "127.0.0.1", uriPort = 8001)
class UserControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    @DisplayName("회원 조회")
    void getCustomer() throws Exception {
        // GIVEN
        long userId = 1L;

        CustomerDto willReturnDto = CustomerDto.builder()
                .id(1L)
                .name("이름")
                .password("password!@#")
                .phoneNumber("010-1234-5678")
                .build();

        given(userService.findCustomerByUserId(userId))
                .willReturn(willReturnDto);

        // WHEN
        ResultActions actions = mockMvc.perform(get("/customer/{userId}", userId));

        // THEN
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("message").value(""))
                .andExpect(jsonPath("data.userId").value(1))
                .andExpect(jsonPath("data.userName").value("이름"))
                .andExpect(jsonPath("data.phoneNumber").value("010-1234-5678"))
                .andDo(print())
                .andDo(document("customer-get",
                        pathParameters(
                                parameterWithName("userId").description("회원 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("결과코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data.userId").description("회원 고유번호"),
                                fieldWithPath("data.userName").description("회원 이름"),
                                fieldWithPath("data.phoneNumber").description("회원 휴대폰 번호")
                        ))
                )
        ;
    }

    @Test
    @DisplayName("회원 조회 - 존재하지 않는 회원")
    void getCustomerNotExistUserException() throws Exception {
        // GIVEN
        long notExistUserId = 9999L;
        String message = "존재하지 않는 회원 입니다.";
        given(userService.findCustomerByUserId(notExistUserId))
                .willThrow(new NotExistUserException(message));

        // WHEN
        ResultActions actions = mockMvc.perform(get("/customer/{userId}", notExistUserId));

        // THEN
        actions.andExpect(status().isConflict())
                .andExpect(jsonPath("code").value(Code.ERROR.name()))
                .andExpect(jsonPath("message").value(message))
                .andExpect(jsonPath("data").isEmpty())
                .andDo(print())
                .andDo(document("customer-get-notExistUserException",
                        pathParameters(
                                parameterWithName("userId").description("회원 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("결과코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data").description("데이터")
                        )
                        ))
                ;
    }
}