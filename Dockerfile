FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
COPY src ./src
RUN ./mvnw clean package -DskipTests -Dspring-boot.run.skip=true
RUN find target -name "*.jar" -not -name "*sources*"
ENTRYPOINT ["sh", "-c", "java -jar $(find target -name '*.jar' -not -name '*sources*')"]