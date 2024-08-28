# Use the OpenJDK 17 base image
FROM openjdk:17-jdk

# Specify the location of the JAR file
ARG JAR_FILE=./build/libs/*-SNAPSHOT.jar

# Copy the JAR file to the container
COPY ${JAR_FILE} app.jar

# Copy the KeyStore file and application.yml to the container
COPY keystore.jks /app/resources/keystore.jks
COPY application.yml /app/resources/application.yml

# Set the working directory
WORKDIR /app

# Run the JAR file with the specified application profile
ENTRYPOINT ["java", "-Dspring.config.location=classpath:/resources/application.yml", "-jar", "/app.jar"]
