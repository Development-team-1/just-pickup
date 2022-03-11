FROM openjdk:11
VOLUME /tmp
COPY build/libs/customer-apigateway-service-1.0.jar CustomerApiGatewayService.jar
ENTRYPOINT ["java", "-jar", "CustomerApiGatewayService.jar"]