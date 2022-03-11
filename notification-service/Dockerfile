FROM openjdk:11
VOLUME /tmp
COPY build/libs/notification-service-1.0.jar NotificationService.jar
ENTRYPOINT ["java", "-jar", "NotificationService.jar"]