# Stage 1 — builder
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src/ src/
RUN ./mvnw package -DskipTests

# Stage 2 — runner
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/guessza-0.0.1-SNAPSHOT.jar /app/guessza.jar
EXPOSE 8080
CMD ["java", "-jar", "guessza.jar"]