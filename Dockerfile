#FROM maven:3.9.4-openjdk-21 AS build
FROM maven:3.9.4-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests

#FROM openjdk:21-jdk-slim AS runtime
FROM eclipse-temurin:21-jdk-jammy

COPY --from=build /target/pricetag-0.0.1-SNAPSHOT.jar pricetag.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","pricetag.jar"]