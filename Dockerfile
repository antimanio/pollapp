FROM gradle:8.14.3-alpine AS builder
WORKDIR /home/gradle

COPY settings.gradle.kts build.gradle.kts .
COPY src src
COPY gradle gradle

RUN gradle bootJar

RUN mv build/libs/*.jar app.jar



FROM eclipse-temurin:21-alpine


WORKDIR /app
RUN addgroup -g 1000 usersgroup
RUN adduser -G usersgroup -D -u 1000 -h /app app

USER app



COPY --from=builder --chown=1000:1000 /home/gradle/app.jar . 




EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]