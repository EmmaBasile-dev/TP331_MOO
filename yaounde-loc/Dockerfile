# Dockerfile pour le backend Spring Boot

FROM maven:3.9-eclipse-temurin-17-focal as builder

WORKDIR /app

# Copier les fichiers de configuration
COPY pom.xml .
COPY src ./src

# Compiler le projet
RUN mvn clean package -DskipTests

# Stage final
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copier le JAR depuis le stage de build
COPY --from=builder /app/target/yaounde_loc-0.0.1-SNAPSHOT.jar app.jar

# Créer le dossier uploads
RUN mkdir -p /app/uploads

# Exposer le port
EXPOSE 8080

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
