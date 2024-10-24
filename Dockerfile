FROM openjdk:17-jdk-alpine
LABEL authors="ntuthuko"

COPY build/libs/sensitive-words-service-0.0.1-SNAPSHOT-plain.jar sensitive-words-service-0.0.1.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","sensitive-words-service-0.0.1.jar"]

