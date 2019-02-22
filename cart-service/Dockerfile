FROM openjdk:8-jdk-alpine
COPY /target/cart-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8023
ENTRYPOINT ["java","-jar","/app.jar"]
