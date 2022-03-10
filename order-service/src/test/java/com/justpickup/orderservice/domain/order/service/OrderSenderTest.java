package com.justpickup.orderservice.domain.order.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.justpickup.orderservice.domain.orderItem.dto.OrderItemDto;
import com.justpickup.orderservice.domain.orderItemOption.dto.OrderItemOptionDto;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@EnableKafka
@DirtiesContext
@EmbeddedKafka(topics = {"orderPlaced"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderSenderTest {

    private final String TEST_TOPIC = "orderPlaced";

    private Consumer<Integer, String> consumer;
    private KafkaTemplate<String, String> producer;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    
    @BeforeEach
    void setup() {
        System.out.println("beforeAll");
        producer = configureProducer();
        consumer = configureConsumer();


    }

    private Consumer<Integer, String> configureConsumer() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        Consumer<Integer, String> consumer = new DefaultKafkaConsumerFactory<Integer, String>(consumerProps)
                .createConsumer();
        consumer.subscribe(Collections.singleton(TEST_TOPIC));
        return consumer;
    }


    private KafkaTemplate<String, String> configureProducer() {
        Map<String, Object> producerProps = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        return new  KafkaTemplate <>( new DefaultKafkaProducerFactory<>(producerProps));
    }
    


    @Test
    void sendOrderPlaced() throws Exception{
        //given
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        OrderSender.KafkaSendOrderDto kafkaSendOrderDto = new OrderSender.KafkaSendOrderDto(
                1L,2L,"주문아이디",1L,12000L,2L, LocalDateTime.now(),3000L, OrderStatus.PLACED,
                List.of(
                        OrderItemDto.of(1L,300L,3000L,2L,
                                List.of(new OrderItemOptionDto(2L)
                                        ,new OrderItemOptionDto(3L))
                        )
                )
        );
        String jsonInString = mapper.writeValueAsString(kafkaSendOrderDto);

        //when
        ListenableFuture<SendResult<String, String>> orderPlaced = producer.send("orderPlaced", jsonInString);
        System.out.println(orderPlaced.get().toString());
        //then

        ConsumerRecord<Integer, String> singleRecord = KafkaTestUtils.getSingleRecord(consumer, TEST_TOPIC);
        assertThat(singleRecord).isNotNull();
        assertThat(singleRecord.value()).isEqualTo(jsonInString);

    }


}