FROM maven:3.9.9-amazoncorretto-21 AS builder

WORKDIR /app
COPY . .
RUN mvn clean package

FROM amazoncorretto:21-alpine-jdk AS runtime

WORKDIR /app
COPY --from=builder /app/target/*-with-dependencies.jar app.jar

ENTRYPOINT [ "java", "-jar", "/app/app.jar" ]