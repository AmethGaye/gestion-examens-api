# ─── Étape 1 : Build avec Maven ───────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copier pom.xml seul d'abord → cache des dépendances Maven
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copier les sources et compiler (sans tests)
COPY src ./src
RUN mvn package -DskipTests -q

# ─── Étape 2 : Image finale légère (JRE uniquement) ───────────────────────────
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copier le JAR compilé depuis l'étape builder
COPY --from=builder /app/target/*.jar app.jar

# Port exposé (défini dans application.properties)
EXPOSE 8081

# Variables d'environnement avec valeurs par défaut (override au runtime)
ENV DB_HOST=localhost \
    DB_PORT=3306 \
    DB_NAME=gestion_examens \
    DB_USER=root \
    DB_PASSWORD=passwd \
    JWT_SECRET=MaCleSecreteTresLonguePourJWT123456789!

ENTRYPOINT ["java", \
  "-Dspring.datasource.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", \
  "-Dspring.datasource.username=${DB_USER}", \
  "-Dspring.datasource.password=${DB_PASSWORD}", \
  "-Dapp.jwt.secret=${JWT_SECRET}", \
  "-jar", "app.jar"]
