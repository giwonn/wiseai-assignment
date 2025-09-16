FROM eclipse-temurin:17 AS builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# 의존성 설치
RUN chmod +x ./gradlew
RUN ./gradlew dependencies --no-daemon

# 빌드
COPY src src
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:17-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
