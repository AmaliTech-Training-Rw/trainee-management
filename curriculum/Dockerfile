# Step 1: Use an official Java runtime image as a base
FROM eclipse-temurin:21


# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the Spring Boot JAR file into the container
# Ensure that the JAR file name matches the output JAR from your build (e.g., dockerdemo-1.0.0.jar)
COPY target/curriculumdemo.jar /app/curriculumdemo.jar

# Step 4: Expose the port on which the Spring Boot application will run
EXPOSE 8282

# Step 5: Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "curriculumdemo.jar"]
