package com.justpickup.orderservice.domain.order.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justpickup.orderservice.config.TestConfig;
import com.justpickup.orderservice.domain.order.dto.FetchOrderDto;
import com.justpickup.orderservice.domain.order.dto.OrderDto;
import com.justpickup.orderservice.domain.order.dto.OrderHistoryDto;
import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.justpickup.orderservice.domain.order.repository.OrderRepository;
import com.justpickup.orderservice.domain.order.service.OrderService;
import com.justpickup.orderservice.domain.orderItem.dto.OrderItemDto;
import com.justpickup.orderservice.domain.orderItemOption.dto.OrderItemOptionDto;
import com.justpickup.orderservice.global.client.store.GetItemResponse;
import com.justpickup.orderservice.global.client.store.OptionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
                                fieldWithPath("data.orders[*].orderStatus").description("매장 고유번호"),
                                fieldWithPath("data.orders[*].storeName").description("매장 이름"),
                                fieldWithPath("data.orders[*].orderItems[*].orderItemId").description("주문 아이템 고유번호"),
                                fieldWithPath("data.orders[*].orderItems[*].orderItemName").description("주문 아이템 이름"),
                                fieldWithPath("data.hasNext").description("더보기 버튼 유무")
                        )
                        ))
                ;
    }

    private SliceImpl<OrderHistoryDto> getWillReturnOrderHistory(Pageable pageable) {

        List<OrderHistoryDto> contents = new ArrayList<>();
        for (long i = 1; i <= 3; i++) {
            OrderHistoryDto._OrderHistoryItem orderItemDto_1 =
                    OrderHistoryDto._OrderHistoryItem.builder()
                            .id(i * 10).itemId(i * 100).build();
            OrderHistoryDto._OrderHistoryItem orderItemDto_2 =
                    OrderHistoryDto._OrderHistoryItem.builder()
                            .id((i + 1) * 20L).itemId((i + 1) *200L).build();

            int hour = 20;
            OrderHistoryDto orderDto = OrderHistoryDto.builder()
                    .id(i)
                    .orderTime(LocalDateTime.of(2022, 3, 7, --hour, 0, 0))
                    .orderStatus(OrderStatus.PLACED)
                    .storeId(i + 1000)
                    .price(i * 10_000)
                    .orderItems(List.of(orderItemDto_1, orderItemDto_2))
                    .build();

            contents.add(orderDto);
        }

        return new SliceImpl<>(contents, pageable, true);
    }

    @Test
    @DisplayName("장바구니 아이템 추가_성공")
    void addItemToBasket() throws Exception {
        //Given
        OrderCustomerApiController.RequestItem requestItem;
//        OrderItemDto orderItemDto;
//        FetchOrderDto fetchOrderDto;
        {
            //givenData
            requestItem = new OrderCustomerApiController.RequestItem(
                    102L, 1L, 3000L, 4L, List.of(1L, 2L, 3L, 4L, 5L)
            );

//            orderItemDto = OrderItemDto.of(-1L,
//                    requestItem.getItemId(),
//                    requestItem.getPrice(),
//                    requestItem.getCount(),
//                    requestItem.getItemOptionIds().stream().map(OrderItemOptionDto::new).collect(Collectors.toList()));

            //willReturn data

        }
        //When
        String body = objectMapper.writeValueAsString(requestItem);

        ResultActions actions = mockMvc.perform(post(url + "/item")
                        .header("user-id", "2")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                );

        //Then

        actions.andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("add-item-to-basket",
                            requestHeaders(headerWithName("user-id").description("유저 고유번호")),
                            requestFields(
                                    fieldWithPath("itemId").description("아이템 고유번호"),
                                    fieldWithPath("storeId").description("매장 고유번호"),
                                    fieldWithPath("price").description("아이템 가격"),
                                    fieldWithPath("count").description("아이템 갯수"),
                                    fieldWithPath("itemOptionIds").description("아이템 옵션들")
                            )
                        ));
    }

    @Test
    @DisplayName("장바구니 정보 조회_성공")
    void fetchOrder() throws Exception{
        //Given
        FetchOrderDto fetchOrderDto =
                new FetchOrderDto(2L,2L,12000L,"저스트카페"
                ,List.of(
                        new FetchOrderDto.OrderItemDto(1L,1L,"카페라테",
                                List.of(new FetchOrderDto.OrderItemDto.OrderItemOptionDto(2L, OptionType.REQUIRED,"Hot")
                                        ,new FetchOrderDto.OrderItemDto.OrderItemOptionDto(2L, OptionType.OTHER,"샷추카"))
                                ,3000L
                                ,32L)
                        )
                );



        given(orderService.fetchOrder(2L)).willReturn(fetchOrderDto);
        //When

        ResultActions actions = mockMvc.perform(get(url + "/orders")
                .header("user-id", "2")
        );

        //Then
        actions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("fetch-order",
                        requestHeaders(headerWithName("user-id").description("유저 고유번호")),
                        responseFields(
                                fieldWithPath("code").description("결과 코드 SUCCESS/ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data.id").description("주문 고유번호"),
                                fieldWithPath("data.userId").description("주문한 유저 고유번호"),
                                fieldWithPath("data.storeName").description("매장 명"),
                                fieldWithPath("data.orderPrice").description("총 합계"),
                                fieldWithPath("data.orderItemDtoList[*].id").description("orderItem 고유번호"),
                                fieldWithPath("data.orderItemDtoList[*].itemId").description("상품 고유번호"),
                                fieldWithPath("data.orderItemDtoList[*].itemName").description("상품 명"),
                                fieldWithPath("data.orderItemDtoList[*].orderItemOptionDtoList[*]").description("아이템 옵션들"),
                                fieldWithPath("data.orderItemDtoList[*].orderItemOptionDtoList[*].id").description("아이템 옵션 고유번호"),
                                fieldWithPath("data.orderItemDtoList[*].orderItemOptionDtoList[*].optionType").description("아이템 옵션 타입"),
                                fieldWithPath("data.orderItemDtoList[*].orderItemOptionDtoList[*].name").description("아이템 옵션명"),
                                fieldWithPath("data.orderItemDtoList[*].price").description("상품 가격"),
                                fieldWithPath("data.orderItemDtoList[*].count").description("상품 갯수")

                        )));


    }

    @Test
    @DisplayName("주문 및 mq produce_성공")
    void saveOrder() throws Exception{
        //Given

        //When
        ResultActions actions = mockMvc.perform(post(url + "/orders")
                .header("user-id", "2")
        );
        //Then
        actions.andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("save-order",
                        requestHeaders(headerWithName("user-id").description("유저 고유번호"))
                        ));

    }



}