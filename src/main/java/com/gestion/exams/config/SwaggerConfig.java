package com.gestion.exams.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("bearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Token JWT pour l'authentification")
                        )
                );
    }

    private Info apiInfo() {
        return new Info()
                .title("API Plateforme de Gestion d'Examens")
                .description("""
                    Cette API permet la gestion complète des examens universitaires, incluant :
                    
                    **Fonctionnalités principales :**
                    - Authentification sécurisée avec JWT
                    - Gestion des utilisateurs et des rôles
                    - Saisie et consultation des notes
                    - Calcul automatique des moyennes
                    - Publication des résultats
                    - Génération des relevés de notes
                    
                    **Rôles disponibles :**
                    - **ETUDIANT** : Consulter ses notes et relevés
                    - **PROFESSEUR** : Saisir les notes de ses cours
                    - **CHEF_DEPARTEMENT** : Superviser les notes de son département et planifier les examens
                    - **ADMINISTRATEUR** : Accès complet au système
                    - **SERVICE_SCOLARITE** : Publier les résultats officiels
                    
                    **Pour commencer :**
                    1. Utilisez `/auth/login` pour vous authentifier
                    2. Récupérez le token JWT retourné
                    3. Ajoutez le token dans l'en-tête : `Authorization: Bearer <token>`
                    
                    **Comptes de test :**
                    - Admin: admin@univ-thies.sn / Passer123
                    - Professeur: aminata.diop@univ-thies.sn / Passer123
                    - Étudiant: amadou.diop@univ-thies.sn / Passer123
                    """)
                .version("1.0.0")
                .contact(new Contact()
                        .name("Équipe de développement : Mouhamad Gaye & Ndaye Coumba Ndiaye"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));
    }
}
