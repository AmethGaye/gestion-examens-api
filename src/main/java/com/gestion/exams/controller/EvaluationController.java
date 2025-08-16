package com.gestion.exams.controller;

import com.gestion.exams.config.ErrorResponse;
import com.gestion.exams.config.MessageResponse;
import com.gestion.exams.dto.*;
import com.gestion.exams.entity.Cours;
import com.gestion.exams.entity.Evaluation;
import com.gestion.exams.mapper.EvaluationMapper;
import com.gestion.exams.service.*;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/evaluations")
@CrossOrigin(origins = "*")
@Tag(name = "Gestion des Évaluations", description = "Endpoints pour la gestion des examens et devoirs")
@SecurityRequirement(name = "bearerAuth")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    private CoursService coursService;

    @GetMapping
    @PreAuthorize("hasAnyRole('PROFESSEUR', 'ADMINISTRATEUR', 'SERVICE_SCOLARITE')")
    @Operation(
            summary = "Liste toutes les évaluations",
            description = "Récupère la liste complète des évaluations (examens et devoirs) avec leurs détails"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste des évaluations récupérée avec succès",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = EvaluationDto.class))
            )
    )
    public ResponseEntity<List<EvaluationDto>> getAllEvaluations() {
        List<EvaluationDto> evaluations = evaluationService.findAll();
        return ResponseEntity.ok(evaluations);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ETUDIANT', 'PROFESSEUR', 'ADMINISTRATEUR', 'SERVICE_SCOLARITE')")
    @Operation(
            summary = "Récupère une évaluation par son identifiant",
            description = "Retourne les détails complets d’une évaluation en fonction de son identifiant unique"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Évaluation récupérée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EvaluationDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Évaluation non trouvée"
            )
    })
    public ResponseEntity<?> getEvaluationById(@PathVariable Long id) {
        try {
            EvaluationDto evaluation = evaluationService.findById(id);
            return ResponseEntity.ok(evaluation);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/cours/{coursId}")
    @PreAuthorize("hasAnyRole('ETUDIANT', 'PROFESSEUR', 'ADMINISTRATEUR', 'CHEF_DEPARTEMENT')")
    @Operation(
            summary = "Liste les évaluations d’un cours",
            description = "Récupère toutes les évaluations associées à un cours donné"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des évaluations récupérée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EvaluationDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cours non trouvé ou aucune évaluation associée"
            )
    })

    public ResponseEntity<List<EvaluationDto>> getEvaluationsByCours(@PathVariable Long coursId) {
        List<EvaluationDto> evaluations = evaluationService.findByCoursId(coursId);
        return ResponseEntity.ok(evaluations);
    }

    @GetMapping("/formation/{formationId}")
    @PreAuthorize("hasAnyRole('PROFESSEUR', 'ADMINISTRATEUR', 'SERVICE_SCOLARITE')")
    @Operation(
            summary = "Liste les évaluations d’une formation",
            description = "Récupère toutes les évaluations liées à une formation donnée"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des évaluations récupérée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EvaluationDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Formation non trouvée ou aucune évaluation associée"
            )
    })
    public ResponseEntity<List<EvaluationDto>> getEvaluationsByFormation(@PathVariable Long formationId) {
        List<EvaluationDto> evaluations = evaluationService.findByFormationId(formationId);
        return ResponseEntity.ok(evaluations);
    }

    @Operation(
            summary = "Liste les évaluations publiées d’un cours",
            description = "Récupère uniquement les évaluations publiées (visibles par les étudiants) pour un cours donné"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des évaluations publiées récupérée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EvaluationDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cours non trouvé ou aucune évaluation publiée"
            )
    })
    @GetMapping("/cours/{coursId}/publiees")
    @PreAuthorize("hasAnyRole('ETUDIANT', 'PROFESSEUR', 'ADMINISTRATEUR', 'SERVICE_SCOLARITE')")
    public ResponseEntity<List<EvaluationDto>> getEvaluationsPublieesByCours(@PathVariable Long coursId) {
        List<EvaluationDto> evaluations = evaluationService.findPublishedByCoursId(coursId);
        return ResponseEntity.ok(evaluations);
    }


    @PostMapping("/{id}/publier")
    @PreAuthorize("hasAnyRole('SERVICE_SCOLARITE', 'ADMINISTRATEUR')")
    @Operation(
            summary = "Publier les résultats d'une évaluation",
            description = "Le Service de la Scolarité Rend publics les résultats d'une évaluation pour consultation par les étudiants"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Résultats publiés avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                        "message": "Résultats publiés avec succès"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Évaluation non trouvée"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé - Seuls les administrateurs et le service scolarité peuvent publier"
            )
    })
    public ResponseEntity<?> publierResultats(
            @PathVariable Long id) {
        try {
            evaluationService.publierResultats(id);
            return ResponseEntity.ok(new MessageResponse("Résultats publiés avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('PROFESSEUR', 'ADMINISTRATEUR', 'CHEF_DEPARTEMENT')")
    @Operation(
            summary = "Créer une nouvelle évaluation",
            description = "Permet de créer une nouvelle évaluation (examen, devoir, etc.) associée à un cours"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Évaluation créée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Evaluation.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                    "id": 1,
                                    "dateExamen": "2025-01-15T09:00:00",
                                    "type": "EXAMEN",
                                    "coefficient": 2.0,
                                    "session": "NORMALE",
                                    "estPublie": false,
                                    "cours": {
                                        "id": 3,
                                        "intitule": "Programmation Java"
                                    }
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides ou cours inexistant"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé - seuls les professeurs, administrateurs et chefs de département peuvent créer une évaluation"
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Données nécessaires pour créer une évaluation",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = EvaluationRequestDto.class),
                    examples = @ExampleObject(
                            value = """
                        {
                            "type": "EXAMEN",
                            "coefficient": 2.0,
                            "dateExamen": "2025-01-15T09:00:00",
                            "session": "NORMALE",
                            "estPublie": false,
                            "coursId": 3
                        }
                        """
                    )
            )
    )

    public ResponseEntity<Evaluation> createEvaluation(@Valid @RequestBody EvaluationRequestDto evaluationDto) {
        Optional<Cours> cours = coursService.coursById(evaluationDto.getCoursId());


        Evaluation evaluation = new Evaluation();
        evaluation.setDateExamen(evaluationDto.getDateExamen());
        evaluation.setType(evaluationDto.getType());
        evaluation.setCours(cours.get());
        evaluation.setCoefficient(evaluationDto.getCoefficient());
        evaluation.setSession(evaluationDto.getSession());
        evaluation.setEstPublie(evaluationDto.getEstPublie());

        return ResponseEntity.ok(evaluationService.saveEvaluation(evaluation));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSEUR')")
    @Operation(
            summary = "Mettre à jour une évaluation existante",
            description = "Permet de modifier une évaluation existante en fonction de son identifiant"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Évaluation mise à jour avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EvaluationDto.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                    "id": 1,
                                    "dateExamen": "2025-02-01T14:00:00",
                                    "type": "DEVOIR",
                                    "coefficient": 1.0,
                                    "session": "RATTRAPAGE",
                                    "estPublie": true,
                                    "cours": {
                                        "id": 3,
                                        "intitule": "Programmation Java"
                                    }
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Évaluation non trouvée"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Cours invalide ou données incorrectes"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé - seuls les professeurs et administrateurs peuvent modifier une évaluation"
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Nouvelles données de l’évaluation à mettre à jour",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = EvaluationRequestDto.class),
                    examples = @ExampleObject(
                            value = """
                        {
                            "type": "DEVOIR",
                            "coefficient": 1.0,
                            "dateExamen": "2025-02-01T14:00:00",
                            "session": "RATTRAPAGE",
                            "estPublie": true,
                            "coursId": 3
                        }
                        """
                    )
            )
    )

    public ResponseEntity<EvaluationDto> updateEvaluation(@PathVariable Long id, @Valid @RequestBody EvaluationRequestDto evaluationDto) {
        Optional<Evaluation> existingEvaluationOpt = evaluationService.getEvaluationById(id);
        if (existingEvaluationOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Cours> cours = coursService.coursById(evaluationDto.getCoursId());
        if (cours.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Evaluation existingEvaluation = existingEvaluationOpt.get();
        existingEvaluation.setDateExamen(evaluationDto.getDateExamen());
        existingEvaluation.setType(evaluationDto.getType());
        existingEvaluation.setCours(cours.get());
        existingEvaluation.setCoefficient(evaluationDto.getCoefficient());
        existingEvaluation.setSession(evaluationDto.getSession());
        existingEvaluation.setEstPublie(evaluationDto.getEstPublie());

        Evaluation savedEvalution = evaluationService.saveEvaluation(existingEvaluation);

        return ResponseEntity.ok(EvaluationMapper.convertToDto(savedEvalution));
    }

}