package com.justpickup.orderservice.domain.order.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justpickup.orderservice.config.TestConfig;
import com.justpickup.orderservice.domain.order.dto.OrderDetailDto;
import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.justpickup.orderservice.domain.order.service.OrderService;
import com.justpickup.orderservice.domain.order.web.OrderController.PatchOrderRequest;
import com.justpickup.orderservice.global.client.store.OptionType;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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

    @Test
    @DisplayName("[GET] 주문 상세 내역 가져오기")
    void getOrderDetail() throws Exception {
        // GIVEN
        Long orderId = 1589L;

        given(orderService.findOrderDetail(orderId)).willReturn(getOrderDetailWillReturn(orderId));
        // THEN
        ResultActions actions = mockMvc.perform(get("/api/order-detail/{orderId}", String.valueOf(orderId)));
        // WHEN
        actions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("api-orderDetail",
                        pathParameters(
                                parameterWithName("orderId").description("주문 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("결과코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메세지"),
                                fieldWithPath("data.id").description("주문 고유번호"),
                                fieldWithPath("data.storeName").description("주문 매장이름"),
                                fieldWithPath("data.orderStatus").description("주문 상태"),
                                fieldWithPath("data.orderTime").description("주문 시간 [yyy-MM-dd]"),
                                fieldWithPath("data.orderPrice").description("주문 금액"),
                                fieldWithPath("data.user.id").description("주문한 회원 고유번호"),
                                fieldWithPath("data.user.name").description("주문한 회원 이름"),
                                fieldWithPath("data.user.phoneNumber").description("주문한 회원 전화번호"),
                                fieldWithPath("data.orderItems[*].id").description("주문아이템 고유번호"),
                                fieldWithPath("data.orderItems[*].itemId").description("아이템 고유번호"),
                                fieldWithPath("data.orderItems[*].totalPrice").description("주문아이템 총합계"),
                                fieldWithPath("data.orderItems[*].count").description("주문아이템 수량"),
                                fieldWithPath("data.orderItems[*].name").description("아이템 이름"),
                                fieldWithPath("data.orderItems[*].options[*].id").description("주문아이템옵션 고유번호"),
                                fieldWithPath("data.orderItems[*].options[*].itemOptionId").description("아이템옵션 고유번호"),
                                fieldWithPath("data.orderItems[*].options[*].name").description("아이템옵션 이름"),
                                fieldWithPath("data.orderItems[*].options[*].optionType").description("아이템옵션 타입")
                        )
                        ))
        ;
    }

    private OrderDetailDto getOrderDetailWillReturn(Long orderId) {
        OrderDetailDto.OrderDetailUser orderDetailUser = OrderDetailDto.OrderDetailUser.builder()
                .id(6L)
                .name("박상범")
                .phoneNumber("010-1234-5678")
                .build();

        long id = 11371L;
        long itemOptionId = 40L;
        OrderDetailDto.OrderDetailItemOption ice = OrderDetailDto.OrderDetailItemOption.builder()
                .id(id++)
                .itemOptionId(itemOptionId++)
                .name("ICE")
                .optionType(OptionType.REQUIRED)
                .build();

        OrderDetailDto.OrderDetailItemOption hot = OrderDetailDto.OrderDetailItemOption.builder()
                .id(id++)
                .itemOptionId(itemOptionId++)
                .name("HOT")
                .optionType(OptionType.REQUIRED)
                .build();

        OrderDetailDto.OrderDetailItemOption plus = OrderDetailDto.OrderDetailItemOption.builder()
                .id(id++)
                .itemOptionId(itemOptionId++)
                .name("시럽 추가")
                .optionType(OptionType.OTHER)
                .build();

        OrderDetailDto.OrderDetailItem 카페라떼 = OrderDetailDto.OrderDetailItem.builder()
                .id(id++)
                .itemId(itemOptionId++)
                .count(2)
                .name("카페라떼")
                .options(List.of(hot, plus))
                .build();

        OrderDetailDto.OrderDetailItem 아메리카노 = OrderDetailDto.OrderDetailItem.builder()
                .id(id++)
                .itemId(itemOptionId++)
                .count(2)
                .options(List.of(ice))
                .name("아메리카노")
                .build();

        return OrderDetailDto.builder()
                .id(orderId)
                .orderStatus(OrderStatus.PLACED)
                .orderTime(LocalDateTime.now())
                .storeName("매장이름")
                .orderPrice(76600L)
                .user(orderDetailUser)
                .orderItems(List.of(카페라떼, 아메리카노))
                .build();
    }
}