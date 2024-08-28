# 베이스 이미지 설정
FROM openjdk:17-jdk

# JAR 파일 및 리소스 파일 변수 설정
ARG JAR_FILE=./build/libs/*-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# 리소스 파일들을 복사
#COPY ./src/main/resources/keystore.p12 /app/resources/keystore.p12
#COPY ./src/main/resources/application.yml /app/resources/application.yml

# 애플리케이션 실행
ENTRYPOINT [ "java", "-jar", "/app.jar" ]
