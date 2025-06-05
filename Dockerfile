# Stage 1: Build the app
FROM gradle:8.4-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle clean bootJar

# Stage 2: Package only the JAR
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
