FROM openjdk:17-jdk-slim
WORKDIR /app
COPY /target/TechRadarQuestionnaire.jar /app/TechRadarQuestionnaire.jar
ENTRYPOINT ["java", "-jar", "TechRadarQuestionnaire.jar"]