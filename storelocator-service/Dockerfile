FROM openjdk:8-jdk-alpine
COPY /target/storelocator-0.0.1.jar app.jar
EXPOSE 8050
ENTRYPOINT ["java","-jar","/app.jar"]