# Stage 1 - Build
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src
COPY .mvn .mvn
COPY mvnw .

RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Stage 2 - Run
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/EmplApp-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]