# Use the official OpenJDK base image for Java 11
FROM adoptopenjdk:11-jre-hotspot

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled JAR file into the container
COPY target/Cargo200rusApplication.jar /app/Cargo200rusApplication.jar

# Expose the port your Telegram bot is listening on (if needed)
EXPOSE 8080

# Specify the command to run your application
CMD ["java", "-jar", "Cargo200rusApplication.jar"]