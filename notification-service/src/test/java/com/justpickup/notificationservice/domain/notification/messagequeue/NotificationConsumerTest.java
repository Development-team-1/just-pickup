package com.justpickup.notificationservice.domain.notification.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.concurrent.ListenableFuture;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@EnableKafka
@DirtiesContext
@EmbeddedKafka
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NotificationConsumerTest {

    private final String ORDER_TOPIC = "orderPlaced";

    private Consumer<Integer, String> consumer;
    private KafkaTemplate<String, String> producer;

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    @BeforeEach
    void setUp() {
        producer = configureProducer();
        consumer = configureConsumer();
    }

    private Consumer<Integer, String> configureConsumer() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        Consumer<Integer, String> consumer = new DefaultKafkaConsumerFactory<Integer, String>(consumerProps)
                .createConsumer();
        consumer.subscribe(Collections.singleton(ORDER_TOPIC));
        return consumer;
    }


    private KafkaTemplate<String, String> configureProducer() {
        Map<String, Object> producerProps = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        return new  KafkaTemplate <>( new DefaultKafkaProducerFactory<>(producerProps));
    }

    @Test
    @DisplayName("주문 신청")
    void orderPlaced() throws JsonProcessingException {
        // GIVEN
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        NotificationConsumer.KafkaSendOrderDto sendOrderDto
                = NotificationConsumer.KafkaSendOrderDto.builder()
                        .id(1L)
                        .orderPrice(10_000L)
                        .orderStatus(OrderStatus.PLACED)
                        .orderTime(LocalDateTime.now())
                        .storeId(2L)
                        .build();
        String sendJson = mapper.writeValueAsString(sendOrderDto);

        // THEN
        ListenableFuture<SendResult<String, String>> orderPlaced = producer.send(ORDER_TOPIC, sendJson);

        // WHEN
        ConsumerRecord<Integer, String> singleRecord =
                KafkaTestUtils.getSingleRecord(consumer, ORDER_TOPIC);

        NotificationConsumer.KafkaSendOrderDto readValue
                = mapper.readValue(singleRecord.value(), NotificationConsumer.KafkaSendOrderDto.class);

        assertThat(singleRecord).isNotNull();
        assertThat(readValue.getOrderStatus()).isEqualTo(OrderStatus.PLACED);
        assertThat(readValue.getId()).isEqualTo(sendOrderDto.getId());
    }
}