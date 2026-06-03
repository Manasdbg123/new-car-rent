# ==========================================
# STAGE 1: Build the Application
# ==========================================
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the .jar file
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================
# STAGE 2: Run the Application
# ==========================================
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the built .jar file from Stage 1
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080 for the cloud provider
EXPOSE 8080

# Command to run the application using the "prod" profile
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]