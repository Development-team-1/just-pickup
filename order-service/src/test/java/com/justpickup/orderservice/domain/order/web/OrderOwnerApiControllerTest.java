package com.justpickup.orderservice.domain.order.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justpickup.orderservice.config.TestConfig;
import com.justpickup.orderservice.domain.order.dto.*;
import com.justpickup.orderservice.domain.order.dto.OrderMainDto._Order;
import com.justpickup.orderservice.domain.order.dto.OrderMainDto._OrderItem;
import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.justpickup.orderservice.domain.order.repository.OrderRepository;
import com.justpickup.orderservice.domain.order.service.OrderService;
import com.justpickup.orderservice.domain.order.validator.PrevOrderSearchValidator;
import com.justpickup.orderservice.domain.orderItem.dto.OrderItemDto;
import com.justpickup.orderservice.global.dto.Code;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderOwnerApiController.class)
@Import(TestConfig.class)
@AutoConfigureRestDocs(uriHost = "http://just-pickup.com", uriPort = 8001)
class OrderOwnerApiControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @SpyBean
    PrevOrderSearchValidator prevOrderSearchValidator;

    private final String url = "/api/owner/order";

    @Test
    @DisplayName("[API] [GET] ?????? ????????? - ?????? ?????????")
    void getOrderMain() throws Exception {
        // GIVEN
        String orderDate = "2022-02-03";
        Long lastOrderId = 7L;
        OrderSearchCondition condition = new OrderSearchCondition(orderDate, lastOrderId);
        // TODO: 2022/02/07 jwt ?????? ??? ?????? ??????
        Long storeId = 1L;

        given(orderService.findOrderMain(condition, storeId))
                .willReturn(getWillReturnOrderMain());

        // WHEN

        ResultActions actions = mockMvc.perform(get(url + "/order-main")
                .header("user-id", "1")
                .param("orderDate", orderDate)
                .param("lastOrderId", String.valueOf(lastOrderId))
        );

        // THEN
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("message").isEmpty())
                .andExpect(jsonPath("data.hasNext").exists())
                .andExpect(jsonPath("data.orders").exists())
                .andExpect(jsonPath("data.orders[*].orderItems").exists())
                .andDo(print())
                .andDo(document("orderMain-get",
                        requestParameters(
                                parameterWithName("orderDate").description("?????? ?????? YYYY-MM-DD"),
                                parameterWithName("lastOrderId").optional().description("???????????? ????????? ?????? ?????? ??????")
                        ),
                        requestHeaders(
                                headerWithName("user-id").description("?????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("code").description("?????? ?????? SUCCESS/ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("data.hasNext").description("?????? ????????? ?????? ??????"),
                                fieldWithPath("data.orders[*].id").description("?????? ????????????"),
                                fieldWithPath("data.orders[*].orderTime").description("?????? ?????? [yyyy-MM-dd HH:mm]"),
                                fieldWithPath("data.orders[*].orderStatus").description("?????? ??????"),
                                fieldWithPath("data.orders[*].userName").description("????????? ????????? ??????"),
                                fieldWithPath("data.orders[*].storeName").description("?????? ??????"),
                                fieldWithPath("data.orders[*].orderItems[*].itemName").description("????????? ??????")
                        )
                ))
        ;
    }

    private OrderMainDto getWillReturnOrderMain() {
        List<_Order> orders = new ArrayList<>();
        for (long i = 1; i <= 6; i++) {
            List<_OrderItem> orderItems = new ArrayList<>();
            for (long j = 10; j <= 12; j++) {
                _OrderItem orderItem = _OrderItem.builder()
                        .id(j)
                        .itemId(j)
                        .itemName("?????????" + i * j).build();
                orderItems.add(orderItem);
            }

            _Order order = _Order.builder()
                    .id(i).userId(i + 10).orderStatus(OrderStatus.ACCEPTED)
                    .orderTime(LocalDateTime.now()).orderItems(orderItems).storeName("?????????" + i)
                    .build();
            orders.add(order);
        }
        return OrderMainDto.builder().orders(orders).hasNext(true).build();
    }

    @Test
    @DisplayName("[API] [GET] ?????? ????????? - ?????? ????????? (????????? ???????????? ??????)")
    void getOrderMainBadRequestException() throws Exception {
        // GIVEN
        String orderDate = "20220203";
        Long lastOrderId = 7L;

        // WHEN
        ResultActions actions = mockMvc.perform(get(url + "/order-main")
                .header("user-id", "1")
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
                                        parameterWithName("orderDate").description("?????? ?????? YYYY-MM-DD"),
                                        parameterWithName("lastOrderId").optional().description("???????????? ????????? ?????? ?????? ??????")
                                ),
                                requestHeaders(
                                        headerWithName("user-id").description("?????? ????????????")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("?????? ?????? SUCCESS/ERROR"),
                                        fieldWithPath("message").description("?????????"),
                                        fieldWithPath("data").description("?????????")
                                )
                        )
                )
        ;
    }

    private List<PrevOrderDto> getOrderMainDtoList() {
        PrevOrderDto._PrevOrderItem orderItemDto_100 = PrevOrderDto._PrevOrderItem.builder()
                .id(100L)
                .itemId(100L)
                .build();
        orderItemDto_100.changeName("?????????1");
        PrevOrderDto._PrevOrderItem orderItemDto_101 = PrevOrderDto._PrevOrderItem.builder()
                .id(101L)
                .itemId(101L)
                .build();
        orderItemDto_101.changeName("?????????2");
        PrevOrderDto._PrevOrderItem orderItemDto_102 = PrevOrderDto._PrevOrderItem.builder()
                .id(102L)
                .itemId(102L)
                .build();
        orderItemDto_102.changeName("?????????3");
        PrevOrderDto._PrevOrderItem orderItemDto_103 = PrevOrderDto._PrevOrderItem.builder()
                .id(103L)
                .itemId(103L)
                .build();
        orderItemDto_103.changeName("?????????2");

        PrevOrderDto orderDto_1 = PrevOrderDto.builder()
                .id(1L)
                .userId(1L)
                .orderItems(List.of(orderItemDto_100, orderItemDto_101))
                .orderStatus(OrderStatus.PLACED)
                .orderTime(LocalDateTime.of(2022, 2, 3, 14, 0, 0))
                .build();
        orderDto_1.changeUserName("?????????");
        PrevOrderDto orderDto_2 = PrevOrderDto.builder()
                .id(2L)
                .userId(1L)
                .orderItems(List.of(orderItemDto_102, orderItemDto_103))
                .orderStatus(OrderStatus.FAILED)
                .orderTime(LocalDateTime.of(2022, 2, 3, 15, 0, 0))
                .build();
        orderDto_2.changeUserName("?????????");

        return List.of(orderDto_1, orderDto_2);
    }

    @Test
    @DisplayName("[API] [GET] ?????? ????????? - ?????? ?????? ?????????")
    void getPrevOrder() throws Exception {
        // GIVEN
        LocalDate startDate = LocalDate.of(2022, 2, 3);
        LocalDate endDate = LocalDate.of(2022, 2, 4);
        String page = "0";

        PrevOrderSearch search = new PrevOrderSearch(LocalDate.of(2022, 2, 3), LocalDate.of(2022, 2, 4));
        PageRequest pageRequest = PageRequest.of(Integer.parseInt(page), 10);
        given(orderService.findPrevOrderMain(search, pageRequest, 1L))
                .willReturn(
                        new PageImpl<>(getOrderMainDtoList(), pageRequest, 1)
                );

        // WHEN
        ResultActions actions = mockMvc.perform(get(url + "/prev-order")
                .header("user-id", "1")
                .param("startDate", startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .param("endDate", endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .param("page", page)
        );

        // THEN
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("message").isEmpty())
                .andExpect(jsonPath("data.orders[*].orderId").exists())
                .andExpect(jsonPath("data.orders[*].orderStatus").exists())
                .andExpect(jsonPath("data.orders[*].orderTime").exists())
                .andExpect(jsonPath("data.orders[*].orderPrice").exists())
                .andExpect(jsonPath("data.orders[*].userName").exists())
                .andExpect(jsonPath("data.orders[*].orderItems[*].orderItemId").exists())
                .andExpect(jsonPath("data.orders[*].orderItems[*].orderItemName").exists())
                .andExpect(jsonPath("data.page.startPage").value(0))
                .andExpect(jsonPath("data.page.totalPage").value(1))
                .andDo(print())
                .andDo(document("prevOrder-get",
                        requestParameters(
                                parameterWithName("startDate").description("???????????? YYYY-MM-DD"),
                                parameterWithName("endDate").description("???????????? YYYY-MM-DD"),
                                parameterWithName("page").optional().description("?????? ????????? (0?????? ??????)")
                        ),
                        requestHeaders(
                                headerWithName("user-id").description("?????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("code").description("?????? ?????? SUCCESS/ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("data.orders[*].orderId").description("?????? ????????????"),
                                fieldWithPath("data.orders[*].orderStatus").description("????????????"),
                                fieldWithPath("data.orders[*].orderTime").description("????????????"),
                                fieldWithPath("data.orders[*].orderPrice").description("????????????"),
                                fieldWithPath("data.orders[*].userName").description("?????????"),
                                fieldWithPath("data.orders[*].orderItems[*].orderItemId").description("???????????? ????????????"),
                                fieldWithPath("data.orders[*].orderItems[*].orderItemName").description("???????????? ??????"),
                                fieldWithPath("data.page.startPage").description("?????? ????????? (0?????? ??????)"),
                                fieldWithPath("data.page.totalPage").description("??? ????????? ??????")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("[API] [GET] ?????? ????????? - ?????? ?????? ????????? (???????????? ??????)")
    void getPrevOrderBindException() throws Exception {
        // GIVEN
        LocalDate startDate = LocalDate.of(2023, 2, 3);
        LocalDate endDate = LocalDate.of(2022, 2, 4);
        String page = "0";

        PrevOrderSearch search = new PrevOrderSearch(startDate, endDate);

        // THEN
        ResultActions actions = mockMvc.perform(get(url + "/prev-order")
                .header("user-id", "1")
                .param("startDate", startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .param("endDate", endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .param("page", page)
        );

        // WHEN
        actions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(Code.ERROR.name()))
                .andExpect(jsonPath("message").isNotEmpty())
                .andExpect(jsonPath("data").isEmpty())
                .andDo(print())
                .andDo(document("prevOrder-get-BindException",
                        requestParameters(
                                parameterWithName("startDate").description("???????????? YYYY-MM-DD"),
                                parameterWithName("endDate").description("???????????? YYYY-MM-DD"),
                                parameterWithName("page").optional().description("?????? ????????? (0?????? ??????)")
                        ),
                        requestHeaders(
                                headerWithName("user-id").description("?????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("code").description("?????? ?????? SUCCESS/ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("data").description("?????????")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("?????? ????????? - ????????????")
    void findDashboard() throws Exception {
        // GIVEN

        given(orderService.findDashboard(1L))
                .willReturn(
                        DashBoardDto.builder()
                        .salesAmount(1237801239L)
                        .bestSellItem(new DashBoardDto.BestSellItem(40L,"???????????????",3217L))
                        .sellAmountAWeeks(
                                List.of(new DashBoardDto.SellAmountAWeek("2022-03-22",1235L),
                                        new DashBoardDto.SellAmountAWeek("2022-03-23",235L),
                                        new DashBoardDto.SellAmountAWeek("2022-03-24",2235L),
                                        new DashBoardDto.SellAmountAWeek("2022-03-25",1635L),
                                        new DashBoardDto.SellAmountAWeek("2022-03-26",35L),
                                        new DashBoardDto.SellAmountAWeek("2022-03-27",635L))
                        )
                        .build()
                );
        // THEN
        ResultActions actions = mockMvc.perform(get(url + "/dashboard")
                .header("user-id", "1")
        );

        // WHEN
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value(Code.SUCCESS.name()))
                .andDo(print())
                .andDo(document("owner-findDashboard",
                        requestHeaders(
                                headerWithName("user-id").description("?????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("code").description("?????? ?????? SUCCESS/ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("data").description("?????????"),
                                fieldWithPath("data.salesAmount").description("??? ????????????"),
                                fieldWithPath("data.bestSellItem").description("7?????? ????????? ?????? ??????"),
                                fieldWithPath("data.bestSellItem.itemId").description("7?????? ????????? ?????? ?????? ????????????"),
                                fieldWithPath("data.bestSellItem.itemName").description("7?????? ????????? ?????? ?????????"),
                                fieldWithPath("data.bestSellItem.sumCounts").description("7?????? ????????? ?????? ???????????????"),
                                fieldWithPath("data.sellAmountAWeeks").description("7?????? ?????? ??????"),
                                fieldWithPath("data.sellAmountAWeeks[*].sellDate").description("7?????? ?????? ????????????"),
                                fieldWithPath("data.sellAmountAWeeks[*].sellAmount").description("7?????? ?????? ??????????????? ?????????")
                        )
                ))
        ;
    }
}