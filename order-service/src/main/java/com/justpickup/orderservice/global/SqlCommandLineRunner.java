package com.justpickup.orderservice.global;

import com.justpickup.orderservice.domain.order.entity.Order;
import com.justpickup.orderservice.domain.order.repository.OrderRepository;
import com.justpickup.orderservice.domain.orderItem.entity.OrderItem;
import com.justpickup.orderservice.domain.orderItemOption.entity.OrderItemOption;
import com.justpickup.orderservice.domain.transaction.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SqlCommandLineRunner implements CommandLineRunner {

    private final OrderRepository orderRepository;

    @Override
    public void run(String... args) throws Exception {
        Long userId = 2L;
        Long userCouponId = null;
        Long storeId = 1L;
        Long orderPrice = 1000L;

        for (int i = 1; i <= 20; i++) {
            Transaction transaction = Transaction.of();

            Long itemId = 1L;
            Long price = 100L;
            Long count = 1L;
            OrderItem orderItem = OrderItem.of(itemId + i, price * i, count + i,
                    OrderItemOption.of(), OrderItemOption.of());

            OrderItem orderItem1 = OrderItem.of(itemId + i + 1, price * (i + 1), count + (i + 1),
                    OrderItemOption.of(), OrderItemOption.of());

            Order order = Order.of(userId, userCouponId, storeId, orderPrice * i, transaction, orderItem, orderItem1);

            if (i % 2 == 0) {
                order.placed();
            } else {
                order.order();
            }

            orderRepository.save(order);
        }
    }
}
