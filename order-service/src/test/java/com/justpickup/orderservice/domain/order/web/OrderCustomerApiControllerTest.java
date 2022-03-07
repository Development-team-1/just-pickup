package com.justpickup.orderservice.domain.order.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justpickup.orderservice.config.TestConfig;
import com.justpickup.orderservice.domain.order.dto.OrderDto;
import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.justpickup.orderservice.domain.order.repository.OrderRepository;
import com.justpickup.orderservice.domain.order.service.OrderService;
import com.justpickup.orderservice.domain.orderItem.dto.OrderItemDto;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

@WebMvcTest(OrderCustomerApiController.class)
@Import(TestConfig.class)
@AutoConfigureRestDocs(uriHost = "http://just-pickup.com", uriPort = 8001)
class OrderCustomerApiControllerTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @MockBean
    OrderRepository orderRepository;

    private final String url = "/api/customer/order";

    @Test
    @DisplayName("[API] [GET] 주문내역 가져오기")
    void getOrderHistory() throws Exception {
        // GIVEN
        Pageable pageable = PageRequest.of(0, 3);
        given(orderService.findOrderHistory(pageable, 2L))
                .willReturn(getWillReturnOrderHistory(pageable));

        // WHEN
        ResultActions actions = mockMvc.perform(get(url + "/history")
                .param("page", "0")
                .header("user-id", "2")
        );

        // THEN
        actions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("api-customer-order-history",
                        requestParameters(
                                parameterWithName("page").optional().description("검색할 페이지 [Optional, default: 0]"),
                                parameterWithName("size").optional().description("검색할 페이지 사이즈 [Optional, default: 3]")
                        ),
                        requestHeaders(
                                headerWithName("user-id").description("유저 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("결과 코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data.orders[*].orderId").description("주문 고유번호"),
                                fieldWithPath("data.orders[*].orderTime").description("주문 시간 [yyyy-MM-dd HH:mm]"),
                                fieldWithPath("data.orders[*].orderPrice").description("합계"),
                                fieldWithPath("data.orders[*].orderStatus").description("주문 상태"),
                                fieldWithPath("data.orders[*].storeName").description("매장 이름"),
                                fieldWithPath("data.orders[*].orderItems[*].orderItemId").description("주문 아이템 고유번호"),
                                fieldWithPath("data.orders[*].orderItems[*].orderItemName").description("주문 아이템 이름"),
                                fieldWithPath("data.hasNext").description("더보기 버튼 유무")
                        )
                        ))
                ;
    }

    private SliceImpl<OrderDto> getWillReturnOrderHistory(Pageable pageable) {

        List<OrderDto> contents = new ArrayList<>();
        for (long i = 1; i <= 3; i++) {
            OrderItemDto orderItemDto_1 = OrderItemDto.builder().id(i * 10).itemId(i * 100).build();
            OrderItemDto orderItemDto_2 = OrderItemDto.builder().id((i + 1) * 20L).itemId((i + 1) *200L).build();

            int hour = 20;
            OrderDto orderDto = OrderDto.builder()
                    .id(i)
                    .orderTime(LocalDateTime.of(2022, 3, 7, --hour, 0, 0))
                    .orderStatus(OrderStatus.PLACED)
                    .storeId(i + 1000)
                    .orderPrice(i * 10_000)
                    .orderItemDtoList(List.of(orderItemDto_1, orderItemDto_2))
                    .build();

            contents.add(orderDto);
        }

        return new SliceImpl<>(contents, pageable, true);
    }
}