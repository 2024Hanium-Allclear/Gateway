FROM openjdk:17-jdk
ARG JAR_FILE=./build/libs/*-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
# keystore.p12 파일을 컨테이너 루트 디렉토리에 복사
COPY ./src/main/resources/keystore.p12 /keystore.p12
ENTRYPOINT ["java", "-jar", "/app.jar"]

# Dockerfile
FROM openjdk:17-jdk
ARG JAR_FILE=./build/libs/*-SNAPSHOT.jar

# Copy the keystore.p12 file to the container
COPY ./src/main/resources/keystore.p12 /app/resources/keystore.p12

# Copy the JAR file
COPY ${JAR_FILE} app.jar

# Run the application
ENTRYPOINT [ "java", "-jar", "/app.jar" ]
