package com.gestion.exams.controller;

import com.gestion.exams.dto.CoursDto;
import com.gestion.exams.dto.UtilisateurDto;
import com.gestion.exams.dto.UtilisateurRequestDto;
import com.gestion.exams.entity.Utilisateur;
import com.gestion.exams.service.UtilisateurService;
import com.gestion.exams.config.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/utilisateurs")
@Tag(name = "Gestion des Utilisateurs", description = "CRUD des utilisateurs par les administrateurs")
@SecurityRequirement(name = "bearerAuth")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    // -------------------- GET : Liste --------------------
    @GetMapping
    @Operation(
            summary = "Liste tous les utilisateurs",
            description = "Récupère la liste complète des utilisateurs avec leurs informations"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste des utilisateurs récupérée avec succès",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = UtilisateurDto.class))
            )
    )
    public ResponseEntity<List<UtilisateurDto>> getAllUtilisateurs() {
        return ResponseEntity.ok(utilisateurService.findAll());
    }

    // -------------------- GET : Par ID --------------------
    @GetMapping("/{id}")
    @Operation(
            summary = "Récupère un utilisateur par ID",
            description = "Retourne les informations détaillées d’un utilisateur"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Utilisateur trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UtilisateurDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utilisateur non trouvé"
            )
    })
    public ResponseEntity<?> getUtilisateurById(
            @Parameter(description = "ID de l’utilisateur à rechercher") @PathVariable Long id) {
        try {
            return ResponseEntity.ok(utilisateurService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // -------------------- POST : Création --------------------
    @PostMapping
    @Operation(
            summary = "Créer un nouvel utilisateur",
            description = "Permet à l’administrateur de créer un nouvel utilisateur"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Utilisateur créé avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Utilisateur.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "id": 1,
                                        "nom": "Gaye",
                                        "prenom": "Ameth",
                                        "email": "ameth.gaye@example.com",
                                        "role": "ADMINISTRATEUR"
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides"
            )
    })
    public ResponseEntity<?> createUtilisateur(
            @Valid @RequestBody UtilisateurRequestDto utilisateurDto) {
        try {
            return ResponseEntity.ok(utilisateurService.createNewUser(utilisateurDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    // -------------------- PUT : Mise à jour --------------------
    @PutMapping("/{id}")
    @Operation(
            summary = "Mettre à jour un utilisateur",
            description = "Permet de modifier les informations d’un utilisateur existant"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Utilisateur mis à jour avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Utilisateur.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utilisateur non trouvé"
            )
    })
    public ResponseEntity<?> updateUtilisateur(
            @Parameter(description = "ID de l’utilisateur à mettre à jour") @PathVariable Long id,
            @Valid @RequestBody UtilisateurRequestDto utilisateurDto) {
        try {
            return ResponseEntity.ok(utilisateurService.updateUser(id, utilisateurDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }

    }

    // -------------------- DELETE : Suppression --------------------
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer un utilisateur",
            description = "Permet de supprimer un utilisateur existant"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Utilisateur supprimé avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Utilisateur supprimé avec succès\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utilisateur non trouvé"
            )
    })
    public ResponseEntity<?> deleteUtilisateur(
            @Parameter(description = "ID de l’utilisateur à supprimer") @PathVariable Long id) {
        boolean deleted = utilisateurService.deleteUser(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("{\"message\": \"Utilisateur supprimé avec succès\"}");
    }
}

