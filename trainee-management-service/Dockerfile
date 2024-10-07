FROM openjdk:21-jre-slim
WORKDIR /trainee-management-service
COPY target/trainee-management-service-0.0.1-SNAPSHOT.jar /trainee-management-service/trainee-management-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "trainee-management-service-0.0.1-SNAPSHOT.jar"]
EXPOSE 8081
