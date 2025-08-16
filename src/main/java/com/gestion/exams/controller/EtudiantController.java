package com.gestion.exams.controller;

import com.gestion.exams.config.ErrorResponse;
import com.gestion.exams.dto.EtudiantDto;
import com.gestion.exams.entity.Etudiant;
import com.gestion.exams.mapper.EtudiantMapper;
import com.gestion.exams.service.EtudiantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/etudiants")
@CrossOrigin(origins = "*")
@Tag(name = "Étudiants", description = "Endpoints pour la gestion des étudiants")
public class EtudiantController {

    @Autowired
    private EtudiantService etudiantService;

    @Operation(
            summary = "Lister tous les étudiants",
            description = "Retourne la liste complète des étudiants inscrits dans le système. Accessible aux rôles : PROFESSEUR, ADMINISTRATEUR, SERVICE_SCOLARITE, CHEF_DEPARTEMENT.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Liste des étudiants récupérée avec succès",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EtudiantDto.class)))
                    )
            }
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('PROFESSEUR', 'ADMINISTRATEUR', 'SERVICE_SCOLARITE', 'CHEF_DEPARTEMENT')")
    public ResponseEntity<?> getEtudiantList() {
        List<EtudiantDto> etudiants = etudiantService.getAllEtudiants();
        return ResponseEntity.ok(etudiants);
    }

    @Operation(
            summary = "Récupérer un étudiant par ID",
            description = "Retourne les informations détaillées d'un étudiant à partir de son identifiant. Accessible aux rôles : ETUDIANT, PROFESSEUR, ADMINISTRATEUR, SERVICE_SCOLARITE, CHEF_DEPARTEMENT.",
            parameters = {
                    @Parameter(name = "id", description = "Identifiant unique de l'étudiant", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Étudiant trouvé",
                            content = @Content(schema = @Schema(implementation = EtudiantDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erreur lors de la récupération (étudiant introuvable ou problème interne)",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ETUDIANT', 'PROFESSEUR', 'ADMINISTRATEUR', 'SERVICE_SCOLARITE', 'CHEF_DEPARTEMENT')")
    public ResponseEntity<?> getEtudiantById(@PathVariable Long id) {
        try {
            Etudiant etudiant = etudiantService.findById(id);
            return ResponseEntity.ok(EtudiantMapper.convertToDto(etudiant));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(
            summary = "Lister les étudiants d'une promotion",
            description = "Retourne la liste des étudiants appartenant à une promotion donnée. Accessible aux rôles : PROFESSEUR, ADMINISTRATEUR, SERVICE_SCOLARITE.",
            parameters = {
                    @Parameter(name = "promotionId", description = "Identifiant unique de la promotion", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Liste des étudiants de la promotion",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EtudiantDto.class)))
                    )
            }
    )
    @GetMapping("/promotion/{promotionId}")
    @PreAuthorize("hasAnyRole('PROFESSEUR', 'ADMINISTRATEUR', 'SERVICE_SCOLARITE')")
    public ResponseEntity<List<EtudiantDto>> getEtudiantsByPromotion(@PathVariable Long promotionId) {
        List<EtudiantDto> etudiants = etudiantService.findByPromotionId(promotionId);
        return ResponseEntity.ok(etudiants);
    }
}
