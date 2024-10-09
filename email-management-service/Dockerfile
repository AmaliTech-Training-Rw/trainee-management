FROM openjdk:21-jre-slim
WORKDIR /email-management-service
COPY target/email-management-service-0.0.1-SNAPSHOT.jar /email-management-service/email-management-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "user-management-service-0.0.1-SNAPSHOT.jar"]
EXPOSE 8086
