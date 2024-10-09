FROM openjdk:21-jre-slim
WORKDIR /user-login-service
COPY target/user-login-service-0.0.1-SNAPSHOT.jar /user-management-service/user-login-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "user-login-service-0.0.1-SNAPSHOT.jar"]
EXPOSE 8084
