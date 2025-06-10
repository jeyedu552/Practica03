FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/epn-todolist-jyugsi-1.0.1.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
