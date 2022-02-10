package com.justpickup.orderservice.domain.order.service;

import com.justpickup.orderservice.domain.order.dto.OrderDto;
import com.justpickup.orderservice.domain.order.dto.OrderSearchCondition;
import com.justpickup.orderservice.domain.order.dto.PrevOrderSearch;
import com.justpickup.orderservice.domain.order.entity.Order;
import com.justpickup.orderservice.domain.order.repository.OrderRepository;
import com.justpickup.orderservice.domain.order.repository.OrderRepositoryCustom;
import com.justpickup.orderservice.global.client.store.GetItemResponse;
import com.justpickup.orderservice.global.client.store.StoreClient;
import com.justpickup.orderservice.global.client.user.GetCustomerResponse;
import com.justpickup.orderservice.global.client.user.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderRepositoryCustom orderRepositoryCustom;
    private final StoreClient storeClient;
    private final UserClient userClient;

    @Override
    public List<OrderDto> findOrderMain(OrderSearchCondition condition, Long storeId) {
        // 주문 가져오기
        List<OrderDto> orderDtoList =
                orderRepositoryCustom.findOrderMain(condition, storeId)
                        .stream()
                        .map(OrderDto::createFullField)
                        .collect(Collectors.toList());

        // 사용자명 및 아이템 이름 가져오기
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

        return orderDtoList;
    }

    @Override
    public Page<OrderDto> findPrevOrderMain(PrevOrderSearch search, Pageable pageable, Long storeId) {
        Page<Order> orderPage = orderRepositoryCustom.findPrevOrderMain(search, pageable, storeId);

        List<OrderDto> orderDtoList = orderPage.getContent()
                .stream()
                .map(OrderDto::createFullField)
                .collect(Collectors.toList());

        return PageableExecutionUtils.getPage(orderDtoList, pageable, orderPage::getTotalElements);
    }
}
