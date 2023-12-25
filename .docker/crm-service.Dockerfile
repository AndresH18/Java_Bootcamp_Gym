# Use a base image with JDK and Gradle installed
FROM gradle:latest as builder


# Set the working directory in the container
WORKDIR /src

COPY /Gym ./
RUN ls -la

# Build the application JAR
RUN chmod +x ./gradlew
RUN ./gradlew bootJar
RUN ls build/libs
RUN ARTIFACT_FILE=$(find build/libs -name "*.jar" -type f) && \
    mkdir /app && \
    mv "$ARTIFACT_FILE" /app/crm-service.jar
#RUN file /app/crm-service.jar

FROM amazoncorretto:21-al2023-jdk

ARG SPRING_PROFILE
ENV ACCESS_KEY="" \
    SECRET_KEY="" \
    DATASOURCE_URL="" \
    DATASOURCE_USERNAME="" \
    DATASOURCE_PASSWORD="" \
    SQS_ENDPOINT="" \
    SQS_NAME="" \
    REGION="" \
    ACTIVE_PROFILE=$SPRING_PROFILE

EXPOSE 8081

WORKDIR /app
COPY --from=builder /app/*.jar .

#RUN chmod +x ./gradlew
#RUN ./gradlew bootJar > gradle_output.log 2>&1
#RUN ARTIFACT_FILE=$(find . -name "*.jar" -type f)
#RUN mv "$ARTIFACT_FILE" /app/crm-service.jar

ENTRYPOINT java -jar crm-service.jar --spring.profiles.active="${ACTIVE_PROFILE}"