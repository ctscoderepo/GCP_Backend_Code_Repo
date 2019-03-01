FROM openjdk:8-jdk-alpine
COPY /target/vision-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8024
ENTRYPOINT ["java","-jar","/app.jar"]
