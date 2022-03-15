package com.justpickup.orderservice.domain.order.service;

import com.justpickup.orderservice.domain.order.dto.*;
import com.justpickup.orderservice.domain.order.entity.Order;
import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.justpickup.orderservice.domain.order.exception.OrderException;
import com.justpickup.orderservice.domain.order.repository.OrderRepository;
import com.justpickup.orderservice.domain.order.repository.OrderRepositoryCustom;
import com.justpickup.orderservice.domain.orderItem.dto.OrderItemDto;
import com.justpickup.orderservice.domain.orderItem.entity.OrderItem;
import com.justpickup.orderservice.domain.orderItemOption.entity.OrderItemOption;
import com.justpickup.orderservice.global.client.store.GetItemResponse;
import com.justpickup.orderservice.global.client.store.GetStoreReseponse;
import com.justpickup.orderservice.global.client.store.StoreClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderRepositoryCustom orderRepositoryCustom;

    private final StoreClient storeClient;

    @Override
    public OrderMainDto findOrderMain(OrderSearchCondition condition, Long storeId) {
        // 주문 가져오기
        OrderMainResult orderMainResult = orderRepositoryCustom.findOrderMain(condition, storeId);

        // 사용자명 및 아이템 이름 가져오기
//        getUserNameAndItemName(orderDtoList);

        return OrderMainDto.of(orderMainResult.getOrders(), orderMainResult.isHasNext());
    }

    @Override
    public Page<OrderDto> findPrevOrderMain(PrevOrderSearch search, Pageable pageable, Long storeId) {
        Page<Order> orderPage = orderRepositoryCustom.findPrevOrderMain(search, pageable, storeId);

        List<OrderDto> orderDtoList = orderPage.getContent()
                .stream()
                .map(OrderDto::createFullField)
                .collect(toList());

        // 사용자명 및 아이템 이름 가져오기
//        getUserNameAndItemName(orderDtoList);

        return PageableExecutionUtils.getPage(orderDtoList, pageable, orderPage::getTotalElements);
    }

    @Override
    public SliceImpl<OrderDto> findOrderHistory(Pageable pageable, Long userId) {
        SliceImpl<Order> orderHistory = orderRepositoryCustom.findOrderHistory(pageable, userId);

        List<OrderDto> contents = orderHistory.getContent()
                .stream()
                .map(OrderDto::createFullField)
                .collect(toList());

        // TODO: 2022/03/07 Feign Client 통신

        return new SliceImpl<>(contents, pageable, orderHistory.hasNext());
    }

    @Override
    @Transactional
    public void addItemToBasket(OrderItemDto orderItemDto, Long storeId, Long userId) {

        //orderItemOption Entity를 생성한다.
        List<OrderItemOption> orderItemOptions = orderItemDto.getOrderItemOptionDtoList()
                .stream().map(orderItemOptionDto -> OrderItemOption.of(orderItemDto.getId()))
                .collect(toList());

        //orderItem을 Entity를 생성한다.
        OrderItem orderItem = OrderItem.of(orderItemDto.getItemId()
                , orderItemDto.getPrice()
                , orderItemDto.getCount()
                ,orderItemOptions);

        //HARD_CODE
        Long userCouponId=0L;

        Optional<Order> optionalOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.PENDING);
        if(optionalOrder.isPresent()){
            if(optionalOrder.get().addOrderItem(orderItem)
                    .getStoreId().equals(storeId))
                throw new OrderException("장바구니에 여러 카페의 메뉴를 담을수 없습니다.");
        }else{
            orderRepository.save(Order.of(userId,userCouponId,storeId,0L,orderItem));
        }
    }

    @Override
    public FetchOrderDto fetchOrder(Long userId) {
        Order order = orderRepositoryCustom.fetchOrder(userId)
                .orElseThrow(() -> new OrderException("장바구니 정보를 찾을 수 없습니다."));
        GetStoreReseponse store = storeClient.getStore(String.valueOf(order.getStoreId())).getData();

        Map<Long, GetItemResponse> itemMap = storeClient.getItemAndItemOptions(order.getOrderItems().stream()
                .map(OrderItem::getItemId)
                        .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList())
        ).getData()
            .stream().collect(
                Collectors.toMap(
                        GetItemResponse::getId
                        ,getItemResponse->getItemResponse
                        ,(t, t2) -> t
                )
        );

        List<FetchOrderDto.OrderItemDto> orderItemDtoList = order.getOrderItems()
                .stream().map(orderItem ->
                        new FetchOrderDto.OrderItemDto(
                                itemMap.get(orderItem.getItemId())
                                ,orderItem))
                .collect(Collectors.toList());


        FetchOrderDto fetchOrderDto = FetchOrderDto.builder()
                        .userId(order.getUserId())
                        .orderPrice(order.getOrderPrice())
                        .storeName(store.getName())
                        .orderItemDtoList(orderItemDtoList)
                        .build();

        return fetchOrderDto;
    }

    @Override
    @Transactional
    public void saveOrder(Long userId) {

        orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.PENDING)
                .orElseThrow(() -> new OrderException("장바구니 정보를 찾을 수 없습니다."))
                .order();


    }

    @Override
    @Transactional
    public void modifyOrder(Long orderId, OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(orderId + "는 없는 주문 번호입니다."));

        order.setOrderStatus(orderStatus);
    }

}
