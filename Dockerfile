# 빌드
FROM eclipse-temurin:21-jdk as builder
WORKDIR /build
COPY . .
RUN ./gradlew build --no-daemon

# 실행
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY build/libs/*-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
