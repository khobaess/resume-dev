FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

COPY gradle gradle
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle
COPY gradlew gradlew

RUN ./gradlew dependencies --no-daemon

COPY src src

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]