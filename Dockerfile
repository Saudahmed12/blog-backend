FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
COPY src ./src
RUN ./mvnw clean package -DskipTests -Dspring-boot.run.skip=true
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "target/*.jar"]