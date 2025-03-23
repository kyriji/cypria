FROM --platform=linux/amd64 gradle:8.10-jdk21 AS build

WORKDIR /app

COPY settings.gradle .
COPY build.gradle .
COPY common/ common/
COPY manager/ manager/

WORKDIR /app/manager
RUN gradle shadowJar --no-daemon

FROM --platform=linux/amd64 openjdk:21-slim

WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/manager/build/libs/*-all.jar /app/application.jar

EXPOSE 8080

CMD ["java", "-jar", "application.jar"]
