FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/*.jar kalah.jar
ENTRYPOINT ["java","-jar","/kalah.jar"]
EXPOSE 8080
