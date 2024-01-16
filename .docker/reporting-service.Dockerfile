# Use a base image with JDK and Gradle installed
FROM gradle:latest as builder

# Set the working directory in the container
WORKDIR /src

COPY /ReportingService ./
RUN ls -la

# Build the application JAR
RUN gradle bootJar
RUN ARTIFACT_FILE=$(find build/libs -name "*.jar" -type f) && \
    mkdir /app && \
    mv "$ARTIFACT_FILE" /app/reporting-service.jar

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

EXPOSE 8082

WORKDIR /app
COPY --from=builder /app/*.jar .

ENTRYPOINT java -jar reporting-service.jar --spring.profiles.active="${ACTIVE_PROFILE}"