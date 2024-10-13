# Use the official Maven image with Java 21 to build the application
FROM maven:3.8.8-eclipse-temurin-21 AS builder

# Set the working directory
WORKDIR /assessment-management-service

# Copy the pom.xml and download dependencies
COPY pom.xml .

# Copy the source code and build the project
COPY src ./src
RUN mvn package -DskipTests

# Use a lightweight JRE image for running the application (Java 21)
FROM eclipse-temurin:21-jre-alpine

# Set the working directory in the runtime container
WORKDIR /assessment-management-service

# Copy the built JAR from the builder stage
COPY --from=builder /assessment-management-service/target/assessment-management-service-0.0.1-SNAPSHOT.jar /assessment-management-service/assessment-management-service-0.0.1-SNAPSHOT.jar

# Set the entry point to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/assessment-management-service/assessment-management-service-0.0.1-SNAPSHOT.jar"]

# Expose the port the application runs on
EXPOSE 8085
