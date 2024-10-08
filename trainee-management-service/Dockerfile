# Use a valid OpenJDK base image
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /trainee-management-service

# Copy the JAR file into the container
COPY --from=builder trainee-management-service/target/trainee-management-service-0.0.1-SNAPSHOT.jar /trainee-management-service/trainee-management-service-0.0.1-SNAPSHOT.jar

# Specify the command to run the application
ENTRYPOINT ["java", "-jar", "/trainee-management-service/trainee-management-service-0.0.1-SNAPSHOT.jar"]

# Expose the application's port
EXPOSE 8081
