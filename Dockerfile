# Build the Java 11-targeted WAR using JDK 17.
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -DskipTests package

# Tomcat 10.1 supports Jakarta Servlet 6.0 and Jakarta WebSocket 2.1.
FROM tomcat:10.1-jre17-temurin
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/Java4Main-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
ENV JAVA_OPTS="-Xms64m -Xmx256m -XX:MaxMetaspaceSize=128m -Djava.awt.headless=true"
EXPOSE 8080
CMD ["catalina.sh", "run"]
