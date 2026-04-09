FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/helpdesk-app-1.0.jar app.jar
CMD ["java", "-jar", "app.jar"]