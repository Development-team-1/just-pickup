package com.justpickup.userservice.domain.user.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justpickup.userservice.config.TestConfig;
import com.justpickup.userservice.domain.user.dto.CustomerDto;
import com.justpickup.userservice.domain.user.exception.NotExistUserException;
import com.justpickup.userservice.domain.user.service.UserService;
import com.justpickup.userservice.global.dto.Code;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)}
)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestConfig.class)
@AutoConfigureRestDocs(uriHost = "127.0.0.1", uriPort = 8001)
class UserControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @SpyBean
    CookieProvider cookieProvider;

    @Test
    @DisplayName("???????????? ?????? ??????")
    void getCustomerByToken() throws Exception {
        // GIVEN
        long userId = 1L;

        CustomerDto willReturnDto = CustomerDto.builder()
                .id(1L)
                .name("??????")
                .password("password!@#")
                .email("hoonasdasd@naver.com")
                .phoneNumber("010-1234-5678")
                .build();

        given(userService.findCustomerByUserId(userId))
                .willReturn(willReturnDto);

        // WHEN
        ResultActions actions = mockMvc.perform(get("/customer/")
                .header("user-id",userId));

        // THEN
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("message").value(""))
                .andExpect(jsonPath("data.userId").value(1))
                .andExpect(jsonPath("data.email").value("hoonasdasd@naver.com"))
                .andExpect(jsonPath("data.userName").value("??????"))
                .andExpect(jsonPath("data.phoneNumber").value("010-1234-5678"))
                .andDo(print())
                .andDo(document("customer-get-mypage",
                        requestHeaders(
                                headerWithName("user-id").description("???????????? ?????? id")
                        ),
                        responseFields(
                                fieldWithPath("code").description("???????????? SUCCESS/ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("data.userId").description("?????? ????????????"),
                                fieldWithPath("data.userName").description("?????? ??????"),
                                fieldWithPath("data.email").description("?????? ?????????"),
                                fieldWithPath("data.phoneNumber").description("?????? ????????? ??????")
                        ))
                )
        ;
    }


    @Test
    @DisplayName("?????? ??????")
    void getCustomer() throws Exception {
        // GIVEN
        long userId = 1L;

        CustomerDto willReturnDto = CustomerDto.builder()
                .id(1L)
                .name("??????")
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
                .andExpect(jsonPath("data.userName").value("??????"))
                .andExpect(jsonPath("data.phoneNumber").value("010-1234-5678"))
                .andDo(print())
                .andDo(document("customer-get",
                        pathParameters(
                                parameterWithName("userId").description("?????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("code").description("???????????? SUCCESS/ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("data.userId").description("?????? ????????????"),
                                fieldWithPath("data.userName").description("?????? ??????"),
                                fieldWithPath("data.phoneNumber").description("?????? ????????? ??????")
                        ))
                )
        ;
    }

    @Test
    @DisplayName("?????? ?????? - ???????????? ?????? ??????")
    void getCustomerNotExistUserException() throws Exception {
        // GIVEN
        long notExistUserId = 9999L;
        String message = "???????????? ?????? ?????? ?????????.";
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
                                parameterWithName("userId").description("?????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("code").description("???????????? SUCCESS/ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("data").description("?????????")
                        )
                        ))
                ;
    }

    @Test
    @DisplayName("[GET] ?????? ????????? ??????")
    void getCustomers() throws Exception {
        // GIVEN
        List<Long> customerIds = List.of(1L, 2L, 3L);

        given(userService.findCustomerByUserIds(customerIds))
                .willReturn(customerWillReturnDto(customerIds));

        String customerIdsParam = customerIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        // THEN
        ResultActions actions = mockMvc.perform(get("/customers/{customerIds}", customerIdsParam));
        // WHEN
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("message").value(""))
                .andDo(print())
                .andDo(document("customers-get",
                        pathParameters(
                                parameterWithName("customerIds").description("?????? ?????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("code").description("?????? ?????? SUCCESS/ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("data[*].userId").description("?????? ?????? ??????"),
                                fieldWithPath("data[*].userName").description("?????? ??????"),
                                fieldWithPath("data[*].phoneNumber").description("?????? ????????????")
                        )
                        ))
        ;
    }

    private List<CustomerDto> customerWillReturnDto(List<Long> customerIds) {
        List<CustomerDto> customerDtoList = new ArrayList<>();
        for (Long customerId : customerIds) {
            CustomerDto customerDto = CustomerDto.builder()
                    .id(customerId)
                    .name("??????" + customerId)
                    .phoneNumber("010-1234-5678")
                    .build();
            customerDtoList.add(customerDto);
        }
        return customerDtoList;
    }
}