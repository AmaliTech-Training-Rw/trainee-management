FROM openjdk:21-jre-slim
WORKDIR /assessment-management-service
COPY target/assessment-management-service-0.0.1-SNAPSHOT.jar /assessment-management-service/assessment-management-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "assessment-management-service-0.0.1-SNAPSHOT.jar"]
EXPOSE 8085
