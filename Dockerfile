### STAGE 1: Compile and Build Maven Codebase ###
ARG BUILD_GIT_COMMIT_REF="local"
ARG BUILD_GIT_COMMIT_ID="00000000"
ARG BUILD_GIT_COMMIT_TIMESTAMP="1980-01-01T00:00:00.000Z"

# Using Maven 3.9.6 with Eclipse Temurin 17 (Alpine-based image)
FROM maven:3.9.6-eclipse-temurin-17-alpine as build

# Build arguments to capture Git metadata
ARG BUILD_GIT_COMMIT_REF
ARG BUILD_GIT_COMMIT_ID
ARG BUILD_GIT_COMMIT_TIMESTAMP

# Environment variables for Git metadata
ENV GIT_COMMIT_REF=${BUILD_GIT_COMMIT_REF}
ENV GIT_COMMIT_ID=${BUILD_GIT_COMMIT_ID}
ENV GIT_COMMIT_TIMESTAMP=${BUILD_GIT_COMMIT_TIMESTAMP}

# Set up application directory
RUN mkdir -p /usr/src/app

# Copy only the necessary files to reduce Docker layer size and improve build efficiency
COPY ./pom.xml /usr/src/app/
COPY ./src /usr/src/app/src

# Set the working directory for Maven build
WORKDIR /usr/src/app

# Maven options to reduce memory usage and optimize the build process
ARG MAVEN_OPTS="-Xmx512m -Xss256k -XX:MaxRAMPercentage=50.0 -Dhttps.protocols=TLSv1.2 -Dmaven.repo.local=./.m2/repository -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=WARN -Dorg.slf4j.simpleLogger.showDateTime=true -Djava.awt.headless=true"
ARG MAVEN_CLI_OPTS="--batch-mode --errors --fail-at-end --show-version -DinstallAtEnd=true -DdeployAtEnd=true -Dmaven.forkCount=0"

# Run Maven build with limited memory and thread resources
RUN mvn $MAVEN_CLI_OPTS package -DskipTests=true

### STAGE 2: Run ###

FROM eclipse-temurin:17.0.11_9-jre-alpine

# Git metadata arguments (same as the build stage)
ARG BUILD_GIT_COMMIT_REF
ARG BUILD_GIT_COMMIT_ID
ARG BUILD_GIT_COMMIT_TIMESTAMP

# Environment variables for Git metadata
ENV GIT_COMMIT_REF=${BUILD_GIT_COMMIT_REF}
ENV GIT_COMMIT_ID=${BUILD_GIT_COMMIT_ID}
ENV GIT_COMMIT_TIMESTAMP=${BUILD_GIT_COMMIT_TIMESTAMP}

# Application work directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /usr/src/app/target/*.jar ./app.jar

# Create non-root user for security (Alpine-compatible)
RUN addgroup -S appgroup && adduser -S -G appgroup appuser
USER appuser

# Expose the necessary application port (change this if needed)
EXPOSE 8080

# Entrypoint to run the application JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]