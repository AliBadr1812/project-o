# ==============================
# BACKEND - Java 21 + Spring Boot
# ==============================

# Stage 1: Build with Maven and Java 21
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /workspace

# Copy only pom.xml first (better layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Extract the built JAR for layering
RUN mkdir -p target/extracted
WORKDIR /workspace/target/extracted
RUN java -Djarmode=layertools -jar ../*.jar extract

# ==============================
# Stage 2: Production runtime
# ==============================
FROM eclipse-temurin:21-jre-alpine

# Install curl for health checks (optional)
RUN apk add --no-cache curl

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

# Copy extracted layers from builder
COPY --from=builder /workspace/target/extracted/dependencies/ ./
COPY --from=builder /workspace/target/extracted/spring-boot-loader/ ./
COPY --from=builder /workspace/target/extracted/snapshot-dependencies/ ./
COPY --from=builder /workspace/target/extracted/application/ ./

# Java 21 optimizations for containers
ENV JAVA_OPTS="-XX:+UseZGC -Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75"

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Entry point
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} org.springframework.boot.loader.JarLauncher"]