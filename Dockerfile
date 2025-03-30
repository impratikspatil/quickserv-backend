# Stage 1: Build with JDK 21
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# 1. Create directory structure first
RUN mkdir -p .mvn/wrapper

# 2. Copy essential files individually
COPY mvnw .
COPY pom.xml .
COPY .mvn/wrapper/maven-wrapper.jar .mvn/wrapper/
COPY .mvn/wrapper/maven-wrapper.properties .mvn/wrapper/
COPY src ./src

# 3. Verify files are in place
RUN ls -la .mvn/wrapper/ && \
    [ -f .mvn/wrapper/maven-wrapper.jar ] && \
    [ -f .mvn/wrapper/maven-wrapper.properties ]

# 4. Build the application
RUN chmod +x mvnw && \
    ./mvnw dependency:go-offline -B && \
    ./mvnw clean package -DskipTests

# Stage 2: Run with JRE 21
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/quickserv-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar", "--spring.data.mongodb.uri=${MONGO_URI}"]