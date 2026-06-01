# Plateforme de Gestion d'Examens

## Description
Cette application permet la gestion complète des examens universitaires, incluant la saisie des notes, la publication des résultats et la consultation des relevés de notes.

## Technologies Utilisées
- **Backend**: Spring Boot 3.5.4, Spring Security, Spring Data JPA
- **Base de données**: MySQL 8.0
- **Authentification**: JWT (JSON Web Tokens)
- **Java**: Version 21

## Prérequis
- Java 21 ou supérieur
- Maven 3.6 ou supérieur
- MySQL 8.0 ou supérieur
- IDE (IntelliJ IDEA)

## Installation et Configuration

### 1. Cloner le projet
```bash
git clone https://github.com/AmethGaye/gestion-examens-api
cd gestion-examens
```


### 2. Configuration de la base de données

#### Créer la base de données MySQL
```sql
CREATE DATABASE gestion_examens;
```

#### Modifier application.properties
Assurez-vous que les paramètres de connexion MySQL sont corrects dans `src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gestion_examens
spring.datasource.username=root
spring.datasource.password=votre_mot_de_passe
```

#### Initialiser les données
Exécutez l'application pour créer les tables et insérer les données de test definit dans la classe DataInitializer.

### 3. Compilation et exécution
```bash
mvn clean install
mvn spring-boot:run
```

L'application sera accessible sur `http://localhost:8081/api/v1`

## 📚 Documentation API avec Swagger

### Accès à la documentation interactive
Une fois l'application démarrée, la documentation Swagger est disponible :

**🌐 Interface Swagger UI :** http://localhost:8081/api/v1/swagger-ui.html  
**📋 Documentation JSON :** http://localhost:8081/api/v1/v3/api-docs

### Fonctionnalités Swagger
- **Documentation interactive** de tous les endpoints
- **Tests directs** des API depuis l'interface
- **Authentification JWT intégrée** avec bouton "Authorize"
- **Exemples de requêtes/réponses** pour chaque endpoint
- **Organisation par modules** (Authentification, Notes, Cours, etc.)

### Guide rapide Swagger
1. Accédez à http://localhost:8081/api/v1/swagger-ui.html
2. Connectez-vous via **POST /api/auth/login** (utilisez les comptes de test)
3. Cliquez sur **"Authorize"** et ajoutez : `Bearer [votre-token-jwt]`
4. Testez les endpoints selon votre rôle

## Comptes de Test

### Utilisateurs prédéfinis (après exécution du script SQL)
- **Admin**: admin@univ-thies.sn / passer123
- **Professeur**: aminata.diop@univ-thies.sn / passer123
- **Étudiant**: amadou.diop@univ-thies.sn / passer123

### Données de test disponibles
La class DataInitializer crée :
- 10 étudiants avec leurs notes
- 10 professeurs spécialisés
- 1 formation (Master en Génie Logiciel)
- 11 cours répartis en 5 UE
- 17 évaluations (devoirs et examens)
- Notes complètes pour tous les cours

## Permissions par Rôle

### ETUDIANT
- Consulter ses propres notes
- Consulter ses relevés de notes
- Voir les évaluations publiées

### PROFESSEUR
- Saisir et modifier les notes de ses cours
- Consulter les notes de ses étudiants
- Voir ses cours et évaluations

### ADMINISTRATEUR
- Accès complet à toutes les fonctionnalités
- Gestion des utilisateurs
- Consultation de tous les rapports

### SERVICE_SCOLARITE
- Publier les résultats
- Générer les relevés officiels
- Consulter toutes les 

### CHEF_DEPARTEMENT
- Planifie les examens
- Valider les toutes les notes

## Structure du Projet

```
src/main/java/com/examens/gestion/
├── entity/          # Entités JPA
├── repository/      # Repositories Spring Data
├── service/         # Services métier
├── controller/      # Contrôleurs REST
├── dto/            # Data Transfer Objects
├── mapper/            # pour mapper DTO <-> Classes
├── config/         # Configuration (Security, JWT)
└── GestionExamensApplication.java
```

## Fonctionnalités Implémentées

✅ **Authentification JWT** - Connexion sécurisée avec tokens  
✅ **Gestion des rôles** - Permissions basées sur les rôles  
✅ **Saisie des notes** - Interface pour les professeurs  
✅ **Consultation des notes** - Accès pour les étudiants 
✅ **Relevés de notes** - Génération automatique  
✅ **Publication des notes d'une evaluation** - Rendre disponible  
✅ **Documentation Swagger** - Interface interactive complète  

## Limitations Actuelles

- Les types d'évaluation sont limités à DEVOIR et EXAMEN
- Gestion des réclamations non implémentée
- Import Excel/CSV non implémenté
- Publier les Resultats d'une session non implémenté
- Gestion des formations : CRUD pour uniteEnseignement, promotions, cours, formation, non implémenté
