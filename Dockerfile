# Use a base image with Maven and JDK for building the application
FROM maven:3.8.3-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /home/app

# Copy the Maven POM file and source code to the working directory
COPY pom.xml .
COPY src ./src

# Build the application and package it as a JAR file
RUN mvn clean package -DskipTests

# Use a lightweight JDK base image for running the application
FROM openjdk:17-slim

# Set the working directory for the runtime image
WORKDIR /home/app

# Copy the built JAR file from the build stage
COPY --from=build /home/app/target/Herbal_Impression-0.0.1-SNAPSHOT.jar .

# Expose the application port
EXPOSE 9000

# Run the application
ENTRYPOINT ["java", "-jar", "Herbal_Impression-0.0.1-SNAPSHOT.jar"]
