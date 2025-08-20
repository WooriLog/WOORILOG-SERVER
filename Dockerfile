FROM amd64/amazoncorretto:17

WORKDIR /app

COPY ./build/libs/woorilog_server-0.0.1-SNAPSHOT.jar app.jar

ENV SPRING_CONFIG_ADDITIONAL_LOCATION="file:/config/application.yml"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]