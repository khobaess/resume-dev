# === Сборка приложения ===
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

# Копируем gradle-файлы
COPY gradle gradle
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle
COPY gradlew gradlew

# Скачиваем зависимости (чтобы кэшировать слой)
RUN ./gradlew dependencies --no-daemon

# Копируем исходники
COPY src src

# Собираем JAR
RUN ./gradlew bootJar --no-daemon

# === Финальный образ ===
FROM eclipse-temurin:17-jre-jammy-slim

WORKDIR /app

# Копируем JAR из этапа сборки
COPY --from=builder /app/build/libs/*.jar app.jar

# Открываем порт
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]