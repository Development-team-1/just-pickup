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
    @DisplayName("[API] [GET] 점주 서비스 - 주문 페이지")
    void getOrderMain() throws Exception {
        // GIVEN
        String orderDate = "2022-02-03";
        Long lastOrderId = 7L;
        OrderSearchCondition condition = new OrderSearchCondition(orderDate, lastOrderId);
        // TODO: 2022/02/07 jwt 구현 시 변경 요망
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
                                parameterWithName("orderDate").description("주문 날짜 YYYY-MM-DD"),
                                parameterWithName("lastOrderId").optional().description("페이지의 마지막 주문 고유 번호")
                        ),
                        requestHeaders(
                                headerWithName("user-id").description("유저 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("결과 코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data.hasNext").description("다음 게시물 표시 여부"),
                                fieldWithPath("data.orders[*].id").description("주문 고유번호"),
                                fieldWithPath("data.orders[*].orderTime").description("주문 시간 [yyyy-MM-dd HH:mm]"),
                                fieldWithPath("data.orders[*].orderStatus").description("주문 상태"),
                                fieldWithPath("data.orders[*].userName").description("주문한 사용자 이름"),
                                fieldWithPath("data.orders[*].storeName").description("가게 이름"),
                                fieldWithPath("data.orders[*].orderItems[*].itemName").description("아이템 이름")
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
                        .itemName("아이템" + i * j).build();
                orderItems.add(orderItem);
            }

            _Order order = _Order.builder()
                    .id(i).userId(i + 10).orderStatus(OrderStatus.ACCEPTED)
                    .orderTime(LocalDateTime.now()).orderItems(orderItems).storeName("가게명" + i)
                    .build();
            orders.add(order);
        }
        return OrderMainDto.builder().orders(orders).hasNext(true).build();
    }

    @Test
    @DisplayName("[API] [GET] 점주 서비스 - 주문 페이지 (잘못된 파라미터 형식)")
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
                                        parameterWithName("orderDate").description("주문 날짜 YYYY-MM-DD"),
                                        parameterWithName("lastOrderId").optional().description("페이지의 마지막 주문 고유 번호")
                                ),
                                requestHeaders(
                                        headerWithName("user-id").description("유저 고유번호")
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

    private List<PrevOrderDto> getOrderMainDtoList() {
        PrevOrderDto._PrevOrderItem orderItemDto_100 = PrevOrderDto._PrevOrderItem.builder()
                .id(100L)
                .itemId(100L)
                .build();
        orderItemDto_100.changeName("아이템1");
        PrevOrderDto._PrevOrderItem orderItemDto_101 = PrevOrderDto._PrevOrderItem.builder()
                .id(101L)
                .itemId(101L)
                .build();
        orderItemDto_101.changeName("아이템2");
        PrevOrderDto._PrevOrderItem orderItemDto_102 = PrevOrderDto._PrevOrderItem.builder()
                .id(102L)
                .itemId(102L)
                .build();
        orderItemDto_102.changeName("아이템3");
        PrevOrderDto._PrevOrderItem orderItemDto_103 = PrevOrderDto._PrevOrderItem.builder()
                .id(103L)
                .itemId(103L)
                .build();
        orderItemDto_103.changeName("아이템2");

        PrevOrderDto orderDto_1 = PrevOrderDto.builder()
                .id(1L)
                .userId(1L)
                .orderItems(List.of(orderItemDto_100, orderItemDto_101))
                .orderStatus(OrderStatus.PLACED)
                .orderTime(LocalDateTime.of(2022, 2, 3, 14, 0, 0))
                .build();
        orderDto_1.changeUserName("닉네임");
        PrevOrderDto orderDto_2 = PrevOrderDto.builder()
                .id(2L)
                .userId(1L)
                .orderItems(List.of(orderItemDto_102, orderItemDto_103))
                .orderStatus(OrderStatus.FAILED)
                .orderTime(LocalDateTime.of(2022, 2, 3, 15, 0, 0))
                .build();
        orderDto_2.changeUserName("닉네임");

        return List.of(orderDto_1, orderDto_2);
    }

    @Test
    @DisplayName("[API] [GET] 점주 서비스 - 지난 주문 페이지")
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
                                parameterWithName("startDate").description("시작날짜 YYYY-MM-DD"),
                                parameterWithName("endDate").description("종료날짜 YYYY-MM-DD"),
                                parameterWithName("page").optional().description("검색 페이지 (0부터 시작)")
                        ),
                        requestHeaders(
                                headerWithName("user-id").description("유저 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("결과 코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data.orders[*].orderId").description("주문 고유번호"),
                                fieldWithPath("data.orders[*].orderStatus").description("주문상태"),
                                fieldWithPath("data.orders[*].orderTime").description("주문시간"),
                                fieldWithPath("data.orders[*].orderPrice").description("결제금액"),
                                fieldWithPath("data.orders[*].userName").description("닉네임"),
                                fieldWithPath("data.orders[*].orderItems[*].orderItemId").description("주문상품 고유번호"),
                                fieldWithPath("data.orders[*].orderItems[*].orderItemName").description("주문상품 이름"),
                                fieldWithPath("data.page.startPage").description("현재 페이지 (0부터 시작)"),
                                fieldWithPath("data.page.totalPage").description("총 페이지 개수")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("[API] [GET] 점주 서비스 - 지난 주문 페이지 (파라미터 오류)")
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
                                parameterWithName("startDate").description("시작날짜 YYYY-MM-DD"),
                                parameterWithName("endDate").description("종료날짜 YYYY-MM-DD"),
                                parameterWithName("page").optional().description("검색 페이지 (0부터 시작)")
                        ),
                        requestHeaders(
                                headerWithName("user-id").description("유저 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("결과 코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data").description("데이터")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("점주 서비스 - 대쉬보드")
    void findDashboard() throws Exception {
        // GIVEN

        given(orderService.findDashboard(1L))
                .willReturn(
                        DashBoardDto.builder()
                        .salesAmount(1237801239L)
                        .bestSellItem(new DashBoardDto.BestSellItem(40L,"까메리카노",3217L))
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
                                headerWithName("user-id").description("유저 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("결과 코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data").description("데이터"),
                                fieldWithPath("data.salesAmount").description("총 판매금약"),
                                fieldWithPath("data.bestSellItem").description("7일간 베스트 판매 상품"),
                                fieldWithPath("data.bestSellItem.itemId").description("7일간 베스트 판매 상품 고유번호"),
                                fieldWithPath("data.bestSellItem.itemName").description("7일간 베스트 판매 상품명"),
                                fieldWithPath("data.bestSellItem.sumCounts").description("7일간 베스트 판매 상품판매량"),
                                fieldWithPath("data.sellAmountAWeeks").description("7일간 판매 통계"),
                                fieldWithPath("data.sellAmountAWeeks[*].sellDate").description("7일간 판매 통계날짜"),
                                fieldWithPath("data.sellAmountAWeeks[*].sellAmount").description("7일간 판매 통계날짜별 판매량")
                        )
                ))
        ;
    }
}