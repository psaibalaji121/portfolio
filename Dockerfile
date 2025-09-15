# Use Java 17 JDK image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first for caching
COPY mvnw .
COPY pom.xml .
COPY .mvn .mvn

# Download dependencies only (speed up builds)
RUN ./mvnw dependency:go-offline

# Copy the rest of the project
COPY src src

# Build the project
RUN ./mvnw clean package -DskipTests

# Expose the port your app runs on
EXPOSE 8080

# Run the jar
CMD ["java", "-jar", "target/portfolio-0.0.1-SNAPSHOT.jar"]
