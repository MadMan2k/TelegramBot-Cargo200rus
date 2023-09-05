# Use the official Maven image to build your Java application
FROM maven:3.8.1-openjdk-11-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy only the POM file to fetch dependencies (this can be optimized for caching)
COPY pom.xml .

# Fetch the application dependencies (this layer can be cached)
RUN mvn dependency:go-offline

# Copy the source code into the container
COPY src ./src

# Build Java application
RUN mvn clean package

# Use the official OpenJDK base image for Java
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the Maven build stage to the final image
COPY --from=build /app/target/cargo200rus.jar .

# Expose the port application listens on (replace 8080 with your actual port)
EXPOSE 8080

# Define the command to run application
CMD ["java", "-jar", "cargo200rus.jar"]