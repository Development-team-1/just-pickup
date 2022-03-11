FROM openjdk:11
VOLUME /tmp
COPY build/libs/owner-apigateway-service-1.0.jar OwnerApiGatewayService.jar
ENTRYPOINT ["java", "-jar", "OwnerApiGatewayService.jar"]