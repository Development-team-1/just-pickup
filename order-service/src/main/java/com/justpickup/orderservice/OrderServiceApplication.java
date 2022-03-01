package com.justpickup.orderservice;

import com.justpickup.orderservice.domain.order.entity.Order;
import com.justpickup.orderservice.domain.order.repository.OrderRepository;
import com.justpickup.orderservice.domain.orderItem.entity.OrderItem;
import com.justpickup.orderservice.domain.orderItemOption.entity.OrderItemOption;
import com.justpickup.orderservice.domain.transaction.entity.Transaction;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

	@Bean
	@Transactional
	CommandLineRunner run(OrderRepository orderRepository) {
		return args -> {
			Long userId = 1L;
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

				orderRepository.save(order);
			}
		};
	}

}
