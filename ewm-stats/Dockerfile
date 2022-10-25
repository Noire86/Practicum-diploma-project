FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar ewm-stats.jar
ENTRYPOINT ["java","-jar","/ewm-stats.jar"]