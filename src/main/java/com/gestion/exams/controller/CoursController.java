package com.gestion.exams.controller;

import com.gestion.exams.config.ErrorResponse;
import com.gestion.exams.dto.CoursDto;
import com.gestion.exams.service.CoursService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ===== CONTRÔLEUR COURS =====
@RestController
@RequestMapping("/cours")
@CrossOrigin(origins = "*")
@Tag(name = "Gestion des Cours", description = "Endpoints pour la consultation des cours et matières")
@SecurityRequirement(name = "bearerAuth")
public class CoursController {
    @Autowired
    private CoursService coursService;

    @GetMapping
    @PreAuthorize("hasAnyRole('PROFESSEUR', 'ADMINISTRATEUR', 'CHEF_DEPARTEMENT', 'ETUDIANT')")
    @Operation(
            summary = "Liste tous les cours",
            description = "Récupère la liste complète des cours avec leurs détails"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste des cours récupérée avec succès",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CoursDto.class)
            )
    )
    public ResponseEntity<List<CoursDto>> getAllCours() {
        List<CoursDto> cours = coursService.findAll();
        return ResponseEntity.ok(cours);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ETUDIANT', 'PROFESSEUR', 'ADMINISTRATEUR', 'CHEF_DEPARTEMENT', 'SERVICE_SCOLARITE')")
    @Operation(
            summary = "Récupère un cours par son identifiant",
            description = "Retourne les détails complets d’un cours en fonction de son identifiant unique"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Cours récupéré avec succès",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CoursDto.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Cours non trouvé"
    )
    public ResponseEntity<?> getCoursById(@PathVariable Long id) {
        try {
            CoursDto cours = coursService.findById(id);
            return ResponseEntity.ok(cours);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/professeur/{professeurId}")
    @PreAuthorize("hasAnyRole('ETUDIANT', 'PROFESSEUR', 'ADMINISTRATEUR', 'CHEF_DEPARTEMENT', 'SERVICE_SCOLARITE')")
    @Operation(
            summary = "Liste les cours d’un professeur",
            description = "Récupère la liste complète des cours enseignés par un professeur donné"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste des cours récupérée avec succès",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CoursDto.class))
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Professeur non trouvé ou aucun cours associé"
    )
    public ResponseEntity<List<CoursDto>> getCoursByProfesseur(@PathVariable Long professeurId) {
        List<CoursDto> cours = coursService.findByProfesseurId(professeurId);
        return ResponseEntity.ok(cours);
    }

    @GetMapping("/formation/{formationId}")
    @PreAuthorize("hasAnyRole('ETUDIANT', 'PROFESSEUR', 'ADMINISTRATEUR', 'SERVICE_SCOLARITE', 'SERVICE_SCOLARITE')")
    @Operation(
            summary = "Liste les cours d’une formation",
            description = "Récupère la liste complète des cours associés à une formation donnée"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste des cours récupérée avec succès",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CoursDto.class))
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Formation non trouvée ou aucun cours associé"
    )

    public ResponseEntity<List<CoursDto>> getCoursByFormation(@PathVariable Long formationId) {
        List<CoursDto> cours = coursService.findByFormationId(formationId);
        return ResponseEntity.ok(cours);
    }

}



