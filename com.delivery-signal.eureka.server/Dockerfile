# OpenJDK 17, Ubuntu Jammy 기반
FROM eclipse-temurin:17-jdk-jammy

# 빌드된 jar 파일 위치 지정 (Gradle 기준)
ARG JAR_FILE=build/libs/server-0.0.1-SNAPSHOT.jar

# 컨테이너 내부에 복사
COPY ${JAR_FILE} /app/app.jar

# 컨테이너 실행 명령
ENTRYPOINT ["java","-jar","/app/app.jar"]
