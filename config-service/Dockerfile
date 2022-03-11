FROM openjdk:11
VOLUME /tmp/Users/sangbum/Desktop/git-repository/just-pickup/docker-compose.yml
COPY build/libs/config-service-1.0.jar ConfigService.jar
ENTRYPOINT ["java", "-jar", "ConfigService.jar"]