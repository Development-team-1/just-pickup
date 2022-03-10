package com.justpickup.orderservice.domain.order.service;

import com.justpickup.orderservice.domain.order.dto.FetchOrderDto;
import com.justpickup.orderservice.domain.order.dto.OrderDto;
import com.justpickup.orderservice.domain.order.dto.OrderSearchCondition;
import com.justpickup.orderservice.domain.order.dto.PrevOrderSearch;
import com.justpickup.orderservice.domain.order.entity.Order;
import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.justpickup.orderservice.domain.order.exception.OrderException;
import com.justpickup.orderservice.domain.order.repository.OrderRepository;
import com.justpickup.orderservice.domain.order.repository.OrderRepositoryCustom;
import com.justpickup.orderservice.domain.orderItem.dto.OrderItemDto;
import com.justpickup.orderservice.domain.orderItem.entity.OrderItem;
import com.justpickup.orderservice.domain.orderItem.repository.OrderItemRepository;
import com.justpickup.orderservice.domain.orderItemOption.entity.OrderItemOption;
import com.justpickup.orderservice.domain.orderItemOption.repository.OrderItemOptionRepository;
import com.justpickup.orderservice.global.client.store.GetItemResponse;
import com.justpickup.orderservice.global.client.store.StoreClient;
import com.justpickup.orderservice.global.client.user.GetCustomerResponse;
import com.justpickup.orderservice.global.client.user.UserClient;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository  orderItemRepository;
    private final OrderItemOptionRepository orderItemOptionRepository;
    private final OrderRepositoryCustom orderRepositoryCustom;
    private final StoreClient storeClient;
    private final UserClient userClient;
    private final OrderSender orderSender;



    @Override
    public List<OrderDto> findOrderMain(OrderSearchCondition condition, Long storeId) {
        // 주문 가져오기
        List<OrderDto> orderDtoList =
                orderRepositoryCustom.findOrderMain(condition, storeId)
                        .stream()
                        .map(OrderDto::createFullField)
                        .collect(Collectors.toList());

        // 사용자명 및 아이템 이름 가져오기
//        getUserNameAndItemName(orderDtoList);

        return orderDtoList;
    }

    @Override
    public Page<OrderDto> findPrevOrderMain(PrevOrderSearch search, Pageable pageable, Long storeId) {
        Page<Order> orderPage = orderRepositoryCustom.findPrevOrderMain(search, pageable, storeId);

        List<OrderDto> orderDtoList = orderPage.getContent()
                .stream()
                .map(OrderDto::createFullField)
                .collect(Collectors.toList());

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
                .collect(Collectors.toList());

        // TODO: 2022/03/07 Feign Client 통신

        return new SliceImpl<>(contents, pageable, orderHistory.hasNext());
    }

    private void getUserNameAndItemName(List<OrderDto> orderDtoList) {
        orderDtoList.forEach(orderDto -> {
            GetCustomerResponse getCustomerResponse =
                    userClient.getUser(orderDto.getUserId()).getData();
            orderDto.setUserName(getCustomerResponse.getUserName());

            orderDto.getOrderItemDtoList()
                    .forEach(orderItemDto -> {
                        GetItemResponse getItemResponse =
                                storeClient.getItem(orderItemDto.getItemId()).getData();
                        orderItemDto.setItemName(getItemResponse.getName());
                    });
        });
    }

    @Override
    @Transactional
    public void addItemToBasket(OrderItemDto orderItemDto,Long storeId, Long userId) {

        //orderItemOption Entity를 생성한다.
        List<OrderItemOption> orderItemOptions = orderItemDto.getOrderItemOptionDtoList()
                .stream().map(orderItemOptionDto -> OrderItemOption.of(orderItemDto.getId()))
                .collect(Collectors.toList());

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

        return new FetchOrderDto(order);
    }

    @Override
    @Transactional
    public void saveOrder(Long userId) {
        Order order = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.PENDING)
                .orElseThrow(() -> new OrderException("장바구니 정보를 찾을 수 없습니다."))
                .setOrderStatus(OrderStatus.PLACED);
        try{
            orderSender.orderPlaced(OrderSender.KafkaSendOrderDto.createPrimitiveField(order));
        }catch (Exception ex){
            throw new OrderException(ex.getMessage());
        }
    }




}
