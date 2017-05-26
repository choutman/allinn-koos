FROM openjdk:alpine
ADD build/libs/parsing-service-0.1.0-fat.jar /opt/parsing-service-0.1.0-fat.jar

EXPOSE 8081

CMD ["java", "-jar", "/opt/parsing-service-0.1.0-fat.jar"]