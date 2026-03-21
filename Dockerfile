# ---------- Build stage ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Set workdir
WORKDIR /app

# Copy Maven wrapper & pom first (better layer caching)
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Make mvnw executable (important on Linux/Render)
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source
COPY src src

# Build jar
RUN ./mvnw clean package -DskipTests

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy built jar from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]