# Build stage
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /home/app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /home/app/target/Herbal_Impression-0.0.1-SNAPSHOT.jar /app/Herbal_Impression.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/Herbal_Impression.jar"]
