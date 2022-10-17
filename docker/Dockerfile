
FROM maven:3.6.3-openjdk-11-slim AS build

ADD ../pom.xml /
RUN mvn dependency:go-offline -B

ADD .. /
RUN mvn clean install -DskipTests

FROM openjdk:11.0.12-slim
COPY --from=build /target/urltapper-*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8080
