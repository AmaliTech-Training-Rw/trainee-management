FROM maven:3.8.8-eclipse-temurin-21 AS builder

# Set the working directory
WORKDIR /api-gateway

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src /api-gateway/src
RUN mvn package -DskipTests


FROM eclipse-temurin:21-jre-alpine


WORKDIR /api-gateway

# Copy the built JAR from the builder stage
COPY --from=builder /api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar /api-gateway/api-gateway-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/api-gateway/api-gateway-0.0.1-SNAPSHOT.jar"]

EXPOSE 8093