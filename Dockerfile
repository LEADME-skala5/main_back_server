# 빌드
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /
COPY . .
RUN ./gradlew build --no-daemon

# 실행
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /build/libs/*-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
