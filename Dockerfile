FROM maven:3-eclipse-temurin-25 as maven_build

WORKDIR /usr/src/app

COPY pom.xml start.sh ./
COPY src ./src

RUN mvn clean install -DskipTests

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

RUN apk update && apk add --no-cache bash

COPY --from=maven_build /usr/src/app/target/*.jar app.jar
COPY --from=maven_build /usr/src/app/*.sh .

RUN chmod +x /app/start.sh
CMD ["/app/start.sh"]




