# ─── Étape 1 : Build avec Maven ───────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copier pom.xml seul → mise en cache des dépendances Maven
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copier les sources et compiler (sans tests)
COPY src ./src
RUN mvn package -DskipTests -q

# ─── Étape 2 : Image finale légère (JRE uniquement) ───────────────────────────
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8081

# Spring Boot lit les variables d'env directement via application.properties
ENTRYPOINT ["java", "-jar", "app.jar"]
