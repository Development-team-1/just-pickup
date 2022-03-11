package com.justpickup.orderservice.domain.order.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.justpickup.orderservice.config.TestConfig;
import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.justpickup.orderservice.domain.order.repository.OrderRepository;
import com.justpickup.orderservice.domain.order.service.OrderService;
import com.justpickup.orderservice.domain.order.web.OrderController.PatchOrderRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@Import(TestConfig.class)
@AutoConfigureRestDocs(uriHost = "http://just-pickup.com", uriPort = 8001)
class OrderControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @MockBean
    OrderRepository orderRepository;

    @Test
    @DisplayName("[PATCH] 주문 수정")
    public void patchOrder() throws Exception {
        // GIVEN
        Long orderId = 1L;
        OrderStatus orderStatus = OrderStatus.PLACED;
        PatchOrderRequest request = new PatchOrderRequest(orderStatus);
        String requestBody = objectMapper.writeValueAsString(request);

        // WHEN
        ResultActions actions = mockMvc.perform(patch("/order/{orderId}", String.valueOf(orderId))
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // THEN
        actions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("order-patch",
                        pathParameters(
                                parameterWithName("orderId").description("주문 고유번호")
                        ),
                        requestFields(
                                fieldWithPath("orderStatus").description("주문 상태")
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