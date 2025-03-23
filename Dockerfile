FROM --platform=linux/amd64 gradle:8.10-jdk21 AS build

WORKDIR /app

# Copy necessary files for the build
COPY settings.gradle .
COPY build.gradle .
COPY common/ common/
COPY manager/ manager/

# Set the working directory to the manager module and build the shadow jar
WORKDIR /app/manager
RUN gradle shadowJar --no-daemon

FROM --platform=linux/amd64 openjdk:17-slim

WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/manager/build/libs/*-all.jar /app/application.jar

EXPOSE 8080

CMD ["java", "-jar", "application.jar"]
