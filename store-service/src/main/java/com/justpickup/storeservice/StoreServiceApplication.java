package com.justpickup.storeservice;

import com.justpickup.storeservice.domain.map.entity.Map;
import com.justpickup.storeservice.domain.store.entity.Store;
import com.justpickup.storeservice.domain.store.repository.StoreRepository;
import com.justpickup.storeservice.global.entity.Address;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableEurekaClient
public class StoreServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner run(StoreRepository storeRepository) {
        return args -> {
            List<Store> stores = new ArrayList<>();

            stores.add(
                    Store.of(
                            new Address("서울시", "마포구 도화동", "201-20"),
                            Map.of(37.5398271003404, 126.94769672415691),
                            1L,
                            "커피온리 마포역점"
                    )
            );

            stores.add(
                    Store.of(
                            new Address("서울시", "마포구 도화동", "50-10"),
                            Map.of(37.54010719003089, 126.94556661330861),
                            2L,
                            "만랩커피 마포점"
                    )
            );

            stores.add(
                    Store.of(
                            new Address("서울시", "마포구 도화동", "555"),
                            Map.of(37.539797393793755, 126.9453578838543),
                            3L,
                            "이디야커피 마포오벨리스크점"
                    )
            );

            stores.add(
                    Store.of(
                            new Address("서울시", "영등포구 도림로", "31길 2"),
                            Map.of(37.493033141569505, 126.89593667847592),
                            4L,
                            "이디야커피 대림역점"
                    )
            );

            storeRepository.saveAll(stores);
        };
    }
}
