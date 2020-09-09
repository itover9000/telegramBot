FROM openjdk:11.0-buster

COPY target/*.jar /opt/app.jar
WORKDIR /opt
CMD ["java", "-jar", "--enable-preview", "app.jar"]

