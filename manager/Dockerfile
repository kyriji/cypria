FROM --platform=linux/amd64 gradle:8.10-jdk21 AS build

WORKDIR /app

COPY build.gradle .
COPY src/ src/

RUN gradle shadowJar --no-daemon

FROM --platform=linux/amd64 openjdk:17-slim

WORKDIR /app

COPY --from=build /app/build/libs/*-all.jar /app/application.jar

EXPOSE 8080

CMD ["java", "-jar", "application.jar"]