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
    @DisplayName("[PATCH] ?????? ??????")
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
                                parameterWithName("orderId").description("?????? ????????????")
                        ),
                        requestFields(
                                fieldWithPath("orderStatus").description("?????? ??????")
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
    @DisplayName("[GET] ?????? ?????? ?????? ????????????")
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
                                parameterWithName("orderId").description("?????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("code").description("???????????? SUCCESS/ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("data.id").description("?????? ????????????"),
                                fieldWithPath("data.storeName").description("?????? ????????????"),
                                fieldWithPath("data.orderStatus").description("?????? ??????"),
                                fieldWithPath("data.orderTime").description("?????? ?????? [yyy-MM-dd]"),
                                fieldWithPath("data.orderPrice").description("?????? ??????"),
                                fieldWithPath("data.user.id").description("????????? ?????? ????????????"),
                                fieldWithPath("data.user.name").description("????????? ?????? ??????"),
                                fieldWithPath("data.user.phoneNumber").description("????????? ?????? ????????????"),
                                fieldWithPath("data.orderItems[*].id").description("??????????????? ????????????"),
                                fieldWithPath("data.orderItems[*].itemId").description("????????? ????????????"),
                                fieldWithPath("data.orderItems[*].totalPrice").description("??????????????? ?????????"),
                                fieldWithPath("data.orderItems[*].count").description("??????????????? ??????"),
                                fieldWithPath("data.orderItems[*].name").description("????????? ??????"),
                                fieldWithPath("data.orderItems[*].options[*].id").description("????????????????????? ????????????"),
                                fieldWithPath("data.orderItems[*].options[*].itemOptionId").description("??????????????? ????????????"),
                                fieldWithPath("data.orderItems[*].options[*].name").description("??????????????? ??????"),
                                fieldWithPath("data.orderItems[*].options[*].optionType").description("??????????????? ??????")
                        )
                        ))
        ;
    }

    private OrderDetailDto getOrderDetailWillReturn(Long orderId) {
        OrderDetailDto.OrderDetailUser orderDetailUser = OrderDetailDto.OrderDetailUser.builder()
                .id(6L)
                .name("?????????")
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
                .name("?????? ??????")
                .optionType(OptionType.OTHER)
                .build();

        OrderDetailDto.OrderDetailItem ???????????? = OrderDetailDto.OrderDetailItem.builder()
                .id(id++)
                .itemId(itemOptionId++)
                .count(2)
                .name("????????????")
                .options(List.of(hot, plus))
                .build();

        OrderDetailDto.OrderDetailItem ??????????????? = OrderDetailDto.OrderDetailItem.builder()
                .id(id++)
                .itemId(itemOptionId++)
                .count(2)
                .options(List.of(ice))
                .name("???????????????")
                .build();

        return OrderDetailDto.builder()
                .id(orderId)
                .orderStatus(OrderStatus.PLACED)
                .orderTime(LocalDateTime.now())
                .storeName("????????????")
                .orderPrice(76600L)
                .user(orderDetailUser)
                .orderItems(List.of(????????????, ???????????????))
                .build();
    }
}