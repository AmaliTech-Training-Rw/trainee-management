


# Use the official Maven image to build the application
FROM maven:3.8.8-eclipse-temurin-17 AS builder

# Set the working directory
WORKDIR /email-management-service

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src /email-management-service/src
RUN mvn package -DskipTests


FROM eclipse-temurin:17-jre-alpine


WORKDIR /email-management-service

# Copy the built JAR from the builder stage
COPY --from=builder /user-management-service/target/user-management-service-0.0.1-SNAPSHOT.jar /user-management-service/user-management-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/user-management-service/user-management-service-0.0.1-SNAPSHOT.jar"]

EXPOSE 8086