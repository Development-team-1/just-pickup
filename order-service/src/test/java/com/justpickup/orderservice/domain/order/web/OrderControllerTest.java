package com.justpickup.orderservice.domain.order.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justpickup.orderservice.config.TestConfig;
import com.justpickup.orderservice.domain.order.dto.OrderDto;
import com.justpickup.orderservice.domain.order.dto.OrderSearchCondition;
import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.justpickup.orderservice.domain.order.service.OrderService;
import com.justpickup.orderservice.domain.orderItem.dto.OrderItemDto;
import com.justpickup.orderservice.global.dto.Code;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.request.RequestDocumentation.*;

@WebMvcTest(OrderController.class)
@Import(TestConfig.class)
@AutoConfigureRestDocs(uriHost = "127.0.0.1", uriPort = 8001)
class OrderControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @Test
    @DisplayName("점주 서비스 - 주문 페이지")
    void getOrderMain() throws Exception {
        // GIVEN
        String orderDate = "2022-02-03";
        Long lastOrderId = 7L;
        OrderSearchCondition condition = new OrderSearchCondition(orderDate, lastOrderId);
        // TODO: 2022/02/07 jwt 구현 시 변경 요망
        Long storeId = 1L;

        given(orderService.findOrderMain(condition, storeId))
                .willReturn(getOrderMainDtoList());

        // WHEN
        ResultActions actions = mockMvc.perform(get("/orderMain")
                .param("orderDate", orderDate)
                .param("lastOrderId", String.valueOf(lastOrderId))
        );

        // THEN
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("message").isEmpty())
                .andExpect(jsonPath("data").exists())
                .andExpect(jsonPath("data[*].orderItemResponses").exists())
                .andExpect(jsonPath("data[*].orderStatus").exists())
                .andExpect(jsonPath("data[*].orderTime").exists())
                .andDo(print())
                .andDo(document("orderMain-get",
                            requestParameters(
                                    parameterWithName("orderDate").description("주문 날짜 YYYY-MM-DD"),
                                    parameterWithName("lastOrderId").optional().description("페이지의 마지막 주문 고유 번호")
                            ),
                            responseFields(
                                    fieldWithPath("code").description("결과 코드 SUCCESS/ERROR"),
                                    fieldWithPath("message").description("메시지"),
                                    fieldWithPath("data[*].orderId").description("주문 고유 번호"),
                                    fieldWithPath("data[*].userId").description("고객 고유 번호"),
                                    fieldWithPath("data[*].userName").description("고객 이름"),
                                    fieldWithPath("data[*].orderItemResponses[*].orderItemId").description("장바구니 고유번호"),
                                    fieldWithPath("data[*].orderItemResponses[*].itemId").description("상품 고유번호"),
                                    fieldWithPath("data[*].orderItemResponses[*].itemName").description("상품 이름"),
                                    fieldWithPath("data[*].orderStatus").description("주문 상태"),
                                    fieldWithPath("data[*].orderTime").description("주문 시간")
                            )
                        ))
        ;
    }

    @Test
    @DisplayName("점주 서비스 - 주문 페이지 (잘못된 파라미터 형식)")
    void getOrderMainBadRequestException() throws Exception {
        // GIVEN
        String orderDate = "20220203";
        Long lastOrderId = 7L;

        // WHEN
        ResultActions actions = mockMvc.perform(get("/orderMain")
                .param("orderDate", orderDate)
                .param("lastOrderId", String.valueOf(lastOrderId))
        );

        // THEN
        actions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(Code.ERROR.name()))
                .andExpect(jsonPath("message").isNotEmpty())
                .andExpect(jsonPath("data").isEmpty())
                .andDo(print())
                .andDo(document("orderMain-get-badParameterException",
                        requestParameters(
                                parameterWithName("orderDate").description("주문 날짜 YYYY-MM-DD"),
                                parameterWithName("lastOrderId").optional().description("페이지의 마지막 주문 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("결과 코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data").description("데이터")
                        )
                        )
                )
        ;
    }

    private List<OrderDto> getOrderMainDtoList() {
        OrderItemDto orderItemDto_100 = OrderItemDto.builder()
                .id(100L)
                .itemId(100L)
                .build();
        orderItemDto_100.setItemName("아이템1");
        OrderItemDto orderItemDto_101 = OrderItemDto.builder()
                .id(101L)
                .itemId(101L)
                .build();
        orderItemDto_101.setItemName("아이템2");
        OrderItemDto orderItemDto_102 = OrderItemDto.builder()
                .id(102L)
                .itemId(102L)
                .build();
        orderItemDto_102.setItemName("아이템3");
        OrderItemDto orderItemDto_103 = OrderItemDto.builder()
                .id(103L)
                .itemId(103L)
                .build();
        orderItemDto_103.setItemName("아이템2");

        OrderDto orderDto_1 = OrderDto.builder()
                .id(1L)
                .userId(1L)
                .orderItemDtoList(List.of(orderItemDto_100, orderItemDto_101))
                .orderStatus(OrderStatus.PLACED)
                .orderTime(LocalDateTime.of(2022, 2, 3, 14, 0, 0))
                .build();
        orderDto_1.setUserName("닉네임");
        OrderDto orderDto_2 = OrderDto.builder()
                .id(2L)
                .userId(1L)
                .orderItemDtoList(List.of(orderItemDto_102, orderItemDto_103))
                .orderStatus(OrderStatus.CANCELED)
                .orderTime(LocalDateTime.of(2022, 2, 3, 15, 0, 0))
                .build();
        orderDto_2.setUserName("닉네임");

        return List.of(orderDto_1, orderDto_2);
    }
}