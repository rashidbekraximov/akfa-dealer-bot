# Use the official OpenJDK 17 as the base image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the host to the container
COPY target/*.jar akfa-dealer.jar

# Expose the port your Spring Boot application runs on
EXPOSE 6060

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "akfa-dealer.jar"]
