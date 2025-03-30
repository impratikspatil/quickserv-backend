# Stage 1: Build
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Copy build files
COPY mvnw pom.xml ./
COPY .mvn/wrapper .mvn/wrapper/
COPY src ./src

# Build
RUN chmod +x mvnw && \
    ./mvnw clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/quickserv-*.jar app.jar

# Runtime config
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]