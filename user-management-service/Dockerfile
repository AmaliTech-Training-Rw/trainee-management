FROM openjdk:21-jre-slim
WORKDIR /user-management-service
COPY target/user-management-service-0.0.1-SNAPSHOT.jar /user-management-service/user-management-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "user-management-service-0.0.1-SNAPSHOT.jar"]
EXPOSE 8083
