FROM openjdk:8u191-jre-alpine3.8
ENV ENV_NAME default
COPY ./build/libs/search-service-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xms512m", "-Xmx1024m", "-XX:+UseConcMarkSweepGC", "-XX:CMSInitiatingOccupancyFraction=70", "-XX:+UseCMSInitiatingOccupancyOnly", "-verbose:gc", "-jar", "-Dspring.profiles.active=${ENV_NAME},${ENV_VERSION_NAME}", "/app.jar"]