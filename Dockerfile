# Stage 1: Build
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# 1. Copy only build files first (optimizes caching)
COPY pom.xml mvnw ./
COPY .mvn/wrapper .mvn/wrapper/
RUN ./mvnw dependency:go-offline -B

# 2. Copy source and build
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# 1. Install SSL certificates and timezone data
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    ca-certificates openssl tzdata && \
    rm -rf /var/lib/apt/lists/* && \
    update-ca-certificates

# 2. Copy built JAR
COPY --from=builder /app/target/quickserv-*.jar app.jar

# 3. Health check for Render monitoring
HEALTHCHECK --interval=30s --timeout=3s \
    CMD curl -f https://quickserv-backend.onrender.com:${PORT}/api/services || exit 1

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]