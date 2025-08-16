package com.gestion.exams.controller;

import com.gestion.exams.config.ErrorResponse;
import com.gestion.exams.config.MessageResponse;
import com.gestion.exams.dto.*;
import com.gestion.exams.entity.Etudiant;
import com.gestion.exams.service.EtudiantService;
import com.gestion.exams.service.NoteService;
import com.gestion.exams.service.ProfesseurService;
import com.gestion.exams.service.UtilisateurDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
@CrossOrigin(origins = "*")
@Tag(name = "Gestion des Notes", description = "Endpoints pour la saisie, consultation et gestion des notes")
@SecurityRequirement(name = "bearerAuth")
public class NoteController {

    @Autowired
    private NoteService noteService;
    @Autowired
    private ProfesseurService professeurService;
    @Autowired
    private EtudiantService etudiantService;


    @PostMapping("/saisir")
    @PreAuthorize("hasAnyRole('PROFESSEUR')")
    @Operation(
            summary = "Saisir une note",
            description = "Permet à un professeur de saisir ou modifier la note d'un étudiant pour une évaluation"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Note saisie avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NoteDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erreur de validation (note hors limites, évaluation inexistante, etc.)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                        "message": "La note doit être comprise entre 0 et 20"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé (professeur non autorisé pour ce cours)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                        "message": "Vous n'êtes pas autorisé à saisir des notes pour ce cours"
                    }
                    """
                            )
                    )
            )
    })
    public ResponseEntity<?> saisirNote(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Données de la note à saisir",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = NoteRequestDto.class),
                            examples = @ExampleObject(
                                    name = "Saisie note",
                                    value = """
                    {
                       "evaluationId" : 17,
                       "professeurId" : 41,
                       "valeur" : 13.5,
                       "etudiantId" : 95
                     }
                    """
                            )
                    )
            )
            @RequestBody NoteRequestDto request, Authentication authentication) {
        try {
            // Récupérer l'ID du professeur connecté
            UtilisateurDetailsService.UtilisateurPrincipal principal =
                    (UtilisateurDetailsService.UtilisateurPrincipal) authentication.getPrincipal();
//
            Long professeurId = getProfesseurIdFromUtilisateur(principal.getId());
            NoteDto noteDto = noteService.saisirNote(request, professeurId);
            return ResponseEntity.ok(noteDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/evaluation/{evaluationId}")
    @PreAuthorize("hasAnyRole('PROFESSEUR', 'ADMINISTRATEUR', 'SERVICE_SCOLARITE')")
    @Operation(
            summary = "Lister les notes d'une évaluation",
            description = "Retourne toutes les notes attribuées dans le cadre d'une évaluation donnée"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des notes récupérée avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NoteDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Évaluation non trouvée",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Évaluation inexistante\" }"))
            )
    })
    public ResponseEntity<?> getNotesParEvaluation(@PathVariable Long evaluationId) {
        List<NoteDto> notes = noteService.getNotesByEvaluation(evaluationId);
        System.out.println("notes :" + notes);
        return ResponseEntity.ok(notes);
    }


    @GetMapping("/etudiant/{etudiantId}")
    @PreAuthorize("hasAnyRole('ETUDIANT', 'PROFESSEUR', 'ADMINISTRATEUR', 'SERVICE_SCOLARITE')")
    @Operation(
            summary = "Consulter les notes d'un étudiant",
            description = "Permet de consulter toutes les notes attribuées à un étudiant. Un étudiant ne peut consulter que ses propres notes."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des notes de l'étudiant"),
            @ApiResponse(responseCode = "403", description = "Accès non autorisé"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    public ResponseEntity<?> getNotesParEtudiant(@PathVariable Long etudiantId,
                                                 Authentication authentication) {
        try {
            // Vérifier que l'étudiant ne peut voir que ses propres notes
            UtilisateurDetailsService.UtilisateurPrincipal principal =
                    (UtilisateurDetailsService.UtilisateurPrincipal) authentication.getPrincipal();

            if (principal.getRole().name().equals("ETUDIANT")) {
                Long etudiantConnecteId = getEtudiantIdFromUtilisateur(principal.getId());
                if (!etudiantConnecteId.equals(etudiantId)) {
                    return ResponseEntity.status(403)
                            .body(new ErrorResponse("Accès non autorisé"));
                }
            }

            List<NoteDto> notes = noteService.getNotesByEtudiant(etudiantId);
            return ResponseEntity.ok(notes);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/etudiant/{etudiantId}/cours/{coursId}")
    @PreAuthorize("hasAnyRole('ETUDIANT', 'PROFESSEUR', 'ADMINISTRATEUR', 'SERVICE_SCOLARITE')")
    @Operation(
            summary = "Consulter les notes d'un étudiant dans un cours",
            description = "Retourne toutes les notes d'un étudiant pour un cours donné."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notes récupérées avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès non autorisé"),
            @ApiResponse(responseCode = "400", description = "Erreur lors de la récupération des notes")
    })
    public ResponseEntity<?> getNotesParEtudiantEtCours(@PathVariable Long etudiantId,
                                                        @PathVariable Long coursId,
                                                        Authentication authentication) {
        try {
            // Vérifier les autorisations comme ci-dessus
            UtilisateurDetailsService.UtilisateurPrincipal principal =
                    (UtilisateurDetailsService.UtilisateurPrincipal) authentication.getPrincipal();

            if (principal.getRole().name().equals("ETUDIANT")) {
                Long etudiantConnecteId = getEtudiantIdFromUtilisateur(principal.getId());
                if (!etudiantConnecteId.equals(etudiantId)) {
                    return ResponseEntity.status(403)
                            .body(new ErrorResponse("Accès non autorisé"));
                }
            }

            List<NoteDto> notes = noteService.getNotesParEtudiantEtCours(etudiantId, coursId);
            return ResponseEntity.ok(notes);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/releve/{etudiantId}")
    @PreAuthorize("hasAnyRole('ETUDIANT', 'PROFESSEUR', 'ADMINISTRATEUR', 'SERVICE_SCOLARITE')")
    @Operation(
            summary = "Générer le relevé de notes d'un étudiant",
            description = "Retourne un relevé de notes complet pour un étudiant. Les étudiants ne peuvent générer que leur propre relevé."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relevé de notes généré avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReleveNoteDto.class))),
            @ApiResponse(responseCode = "403", description = "Accès non autorisé"),
            @ApiResponse(responseCode = "400", description = "Erreur lors de la génération du relevé")
    })
    public ResponseEntity<?> genererReleveNotes(@PathVariable Long etudiantId, Authentication authentication) {
        try {
            // Vérifier les autorisations
            UtilisateurDetailsService.UtilisateurPrincipal principal =
                    (UtilisateurDetailsService.UtilisateurPrincipal) authentication.getPrincipal();

            if (principal.getRole().name().equals("ETUDIANT")) {
                Long etudiantConnecteId = getEtudiantIdFromUtilisateur(principal.getId());
                if (!etudiantConnecteId.equals(etudiantId)) {
                    return ResponseEntity.status(403)
                            .body(new ErrorResponse("Accès non autorisé"));
                }
            }

            ReleveNoteDto releveNotes = noteService.genererReleveNotes(etudiantId);
            return ResponseEntity.ok(releveNotes);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{noteId}")
    @PreAuthorize("hasAnyRole('PROFESSEUR', 'ADMINISTRATEUR')")
    @Operation(
            summary = "Supprimer une note",
            description = "Permet à un professeur ou un administrateur de supprimer une note existante"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Note supprimée avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erreur lors de la suppression de la note",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> supprimerNote(@PathVariable Long noteId) {
        try {
            noteService.supprimerNote(noteId);
            return ResponseEntity.ok(new MessageResponse("Note supprimée avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    // Méthodes utilitaires
    private Long getProfesseurIdFromUtilisateur(Long utilisateurId) {
        try {
            ProfesseurDto professeur = professeurService.findByUtilisateurId(utilisateurId);
            return professeur.getId();
        } catch (Exception e) {
            throw new RuntimeException("Professeur non trouvé pour cet utilisateur");
        }
    }

    private Long getEtudiantIdFromUtilisateur(Long utilisateurId) {
        try {
            Etudiant etudiant = etudiantService.findByUtilisateurId(utilisateurId).get();
            return etudiant.getId();
        } catch (Exception e) {
            throw new RuntimeException("Étudiant non trouvé pour cet utilisateur");
        }
    }

}
