package com.justpickup.orderservice.domain.order.service;

import com.justpickup.orderservice.domain.order.dto.OrderDto;
import com.justpickup.orderservice.domain.order.repository.OrderRepository;
import com.justpickup.orderservice.domain.order.repository.OrderRepositoryCustom;
import com.justpickup.orderservice.global.client.store.GetItemResponse;
import com.justpickup.orderservice.global.client.store.StoreClient;
import com.justpickup.orderservice.global.client.user.UserClient;
import com.justpickup.orderservice.global.client.user.GetCustomerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    public List<OrderDto> findOrderMain(LocalDate orderDate) {
        // 주문 가져오기
        List<OrderDto> orderDtoList = orderRepositoryCustom.findOrderMainBetweenOrderDate(orderDate)
                .stream()
                .map(OrderDto::createFullField)
                .collect(Collectors.toList());

        // 사용자명 및 아이템 이름 가져오기
        orderDtoList.forEach(orderDto -> {
            GetCustomerResponse getCustomerResponse = userClient.getUser(orderDto.getUserId())
                            .getData();
            orderDto.setUserName(getCustomerResponse.getUserName());

            orderDto.getOrderItemDtoList()
                    .forEach(orderItemDto -> {
                        GetItemResponse getItemResponse = storeClient.getItem(orderItemDto.getItemId())
                                .getData();
                        orderItemDto.setItemName(getItemResponse.getName());
                    });
        });

        return orderDtoList;
    }
}
