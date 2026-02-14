FROM gradle:jdk17-jammy AS builder
WORKDIR /app
COPY . .
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:17-jre-jammy
RUN apt-get update && apt-get install -y tzdata
ENV TZ=America/Sao_Paulo
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
