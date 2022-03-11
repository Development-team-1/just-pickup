FROM openjdk:11
VOLUME /tmp
COPY build/libs/store-service-1.0.jar StoreService.jar
ENTRYPOINT ["java", "-jar", "StoreService.jar"]