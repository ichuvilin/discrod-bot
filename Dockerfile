FROM gradle:8.5-jdk17 AS build
LABEL authors="ichuvilin"

COPY . /app

WORKDIR /app

RUN gradle clean build -x test --no-daemon

FROM eclipse-temurin:17-jre-alpine AS run

ARG JAR_FILE=/app/build/libs/*.jar
COPY --from=build ${JAR_FILE} /app/app.jar

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]