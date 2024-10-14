# Use the official Maven image with Java 21 to build the application
FROM maven:3.8.8-eclipse-temurin-21 AS builder

# Set the working directory
WORKDIR /server

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the project
COPY src /server/src
RUN mvn package -DskipTests

# Use a lightweight JRE image for running the application (Java 21)
FROM eclipse-temurin:21-jre-alpine

# Set the working directory in the runtime container
WORKDIR /server

# Copy the built JAR from the builder stage
COPY --from=builder /server/target/server-0.0.1-SNAPSHOT.jar /server/server-0.0.1-SNAPSHOT.jar

# Set the entry point to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/server/server-0.0.1-SNAPSHOT.jar"]

# Expose the port the application runs on
EXPOSE 8761
