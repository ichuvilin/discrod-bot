FROM gradle:8.5-jdk17 AS build


COPY . /app

WORKDIR /app

RUN gradle clean build -x test --no-daemon

FROM amazoncorretto:17 AS run

ARG JAR_FILE=/app/build/libs/*.jar
COPY --from=build ${JAR_FILE} /app/app.jar

WORKDIR /app

ENV DISCORD_BOT_TOKEN=token
ENV YT_EMAIL=email
ENV YT_PASSWORD=password

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]