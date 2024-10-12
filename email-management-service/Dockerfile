FROM maven:3.8.8-eclipse-temurin-21 AS builder

# Set the working directory
WORKDIR /email-management-service

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src /email-management-service/src
RUN mvn package -DskipTests


FROM eclipse-temurin:21-jre-alpine


WORKDIR /email-management-service

# Copy the built JAR from the builder stage
COPY --from=builder /email-management-service/target/email-management-service-0.0.1-SNAPSHOT.jar /email-management-service/email-management-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/email-management-service/email-management-service-0.0.1-SNAPSHOT.jar"]

EXPOSE 8086