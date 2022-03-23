package com.justpickup.orderservice.domain.orderItem.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justpickup.orderservice.config.TestConfig;
import com.justpickup.orderservice.domain.order.service.OrderService;
import com.justpickup.orderservice.domain.order.web.OrderCustomerApiController;
import com.justpickup.orderservice.domain.orderItem.service.OrderItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderItemCustomerApiController.class)
@Import(TestConfig.class)
@AutoConfigureRestDocs(uriHost = "http://just-pickup.com", uriPort = 8001)
class OrderItemCustomerApiControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderItemService orderItemService;

    private final String url = "/api/customer/order";

    @Test
    @DisplayName("주문 아이템 삭제")
    void deleteOrderItem() throws Exception{

        //given
        Long orderItemId = 2L;

        //when
        ResultActions resultActions = mockMvc.perform(
                delete("/api/customer/orderItem/{orderItemId}",orderItemId)
                        .header("user-id", "2")
        );

        //then

        resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MockMvcRestDocumentation.document("delete-orderItem",
                        pathParameters(
                                parameterWithName("orderItemId").description("orderItem 고유번호")
                        ),
                        requestHeaders(
                                headerWithName("user-id").description("유저 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("결과 코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data").description("orderItem 고유번호")
                        )
                        ));


    }
}