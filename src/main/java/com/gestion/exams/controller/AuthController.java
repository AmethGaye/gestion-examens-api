package com.gestion.exams.controller;

import com.gestion.exams.config.ErrorResponse;
import com.gestion.exams.config.JwtUtil;
import com.gestion.exams.config.MessageResponse;
import com.gestion.exams.dto.*;
import com.gestion.exams.entity.Role;
import com.gestion.exams.service.UtilisateurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@RestController
@RequestMapping("auth")
@CrossOrigin(origins = "*")
@Tag(name = "Authentification", description = "Endpoints pour l'authentification et la gestion des sessions")
public class AuthController {

        @Autowired
        private UtilisateurService utilisateurService;

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private JwtUtil jwtTokenUtil;

        @Operation(summary = "Inscription utilisateur", description = """
                        Permet de créer un nouveau compte utilisateur dans le système.

                        **Rôles attribués :**
                        - Par défaut, un nouvel utilisateur est inscrit en tant qu'**ETUDIANT**.
                        - Les autres rôles (PROFESSEUR, CHEF_DEPARTEMENT, ADMINISTRATEUR, SERVICE_SCOLARITE)
                          peuvent être attribués uniquement par un administrateur via le back-office.

                        **Pour commencer :**
                        1. Fournir les informations nécessaires : nom, prénom, email, mot de passe, numero dossier, date de naissance.
                        2. Une fois inscrit, utilisez `/auth/login` pour obtenir un token JWT.

                        **Exemple d’entrée :**
                        ```json
                        {
                            "nom": "Diop",
                            "prenom": "Amadou",
                            "email": "amadou.diop@univ-thies.sn",
                            "numeroDossier" : "",
                            "password" : "",
                            "dateNaissance" : "2025-08-16"
                            ""
                        }
                        ```
                        """)
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Inscription réussie", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UtilisateurDto.class), examples = @ExampleObject(name = "Utilisateur inscrit avec succès", value = """
                                        {
                                            "id": 10,
                                            "nom": "Diop",
                                            "prenom": "Amadou",
                                            "email": "amadou.diop@univ-thies.sn",
                                            "role": "ETUDIANT"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Erreur de validation", value = """
                                        {
                                            "message": "L'email est déjà utilisé"
                                        }
                                        """)))
        })

        @PostMapping("/register")
        public ResponseEntity<?> createUser(@Valid @RequestBody RegisterDto registerDTO) {
                try {
                        UtilisateurDto userSaved = utilisateurService.createNewEtudiant(registerDTO);
                        return ResponseEntity.status(HttpStatus.CREATED).body(userSaved);
                } catch (Exception e) {
                        return ResponseEntity.badRequest()
                                        .body(new ErrorResponse(e.getMessage()));
                }

        }

        @Operation(summary = "Connexion utilisateur", description = "Authentifie un utilisateur et retourne un token JWT")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Connexion réussie", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class), examples = @ExampleObject(name = "Connexion réussie", value = """
                                        {
                                            "token": "eyJhbGciOiJIUzUxMiJ9...",
                                            "type": "Bearer",
                                            "utilisateur": {
                                                "id": 1,
                                                "nom": "Doe",
                                                "prenom": "John",
                                                "email": "john.doe@example.com",
                                                "role": "PROFESSEUR"
                                            }
                                        }
                                        """))),
                        @ApiResponse(responseCode = "400", description = "Identifiants incorrects", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Erreur d'authentification", value = """
                                        {
                                            "message": "Email ou mot de passe incorrect"
                                        }
                                        """)))
        })
        @PostMapping("/login")
        public ResponseEntity<?> login(
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Identifiants de connexion", required = true, content = @Content(schema = @Schema(implementation = LoginRequest.class), examples = {
                                        @ExampleObject(name = "Connexion Admin", value = """
                                                        {
                                                            "email" : "admin@univ-thies.sn",
                                                             "password" : "Passer123"
                                                        }
                                                        """),
                                        @ExampleObject(name = "Connexion Professeur", value = """
                                                        {
                                                            "email": "aminata.diop@univ-thies.sn",
                                                            "password": "Passer123"
                                                        }
                                                        """),
                                        @ExampleObject(name = "Connexion Étudiant", value = """
                                                        {
                                                            "email": "amadou.diop@univ-thies.sn",
                                                            "password": "Passer123"
                                                        }
                                                        """)
                        })) @RequestBody LoginRequest loginRequest) {
                try {
                        authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                        loginRequest.getEmail(),
                                                        loginRequest.getPassword()));
                } catch (BadCredentialsException e) {
                        return ResponseEntity.badRequest()
                                        .body(new ErrorResponse("Email ou mot de passe incorrect"));
                }

                // Récupérer les informations de l'utilisateur
                UtilisateurDto utilisateur = utilisateurService.findByEmail(loginRequest.getEmail());
                final String token = jwtTokenUtil.generateToken(utilisateur.getEmail());
                return ResponseEntity.ok(new LoginResponse(token, "Bearer", utilisateur));
        }

}
