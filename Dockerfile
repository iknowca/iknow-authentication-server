FROM openjdk:21

ARG JAR_FILE=./*.jar
COPY ${JAR_FILE} app.jar