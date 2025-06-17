# 실행 전용 이미지 (빌드는 GitHub Actions에서 수행)
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# JAR 파일 복사 (GitHub Actions에서 빌드된 것)
COPY build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 8080

# 성능 최적화를 위한 JVM 옵션
ENTRYPOINT ["java", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-jar", \
    "app.jar"]