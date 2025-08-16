package com.gestion.exams.controller;

import com.gestion.exams.config.ErrorResponse;
import com.gestion.exams.dto.EtudiantDto;
import com.gestion.exams.dto.ProfesseurDto;
import com.gestion.exams.service.ProfesseurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professeurs")
@CrossOrigin(origins = "*")
@Tag(name = "Professeurs", description = "Gestion des professeurs de la plateforme")
public class ProfesseurController {

    @Autowired
    private ProfesseurService professeurService;


    @GetMapping
    @PreAuthorize("hasAnyRole('PROFESSEUR', 'ADMINISTRATEUR', 'SERVICE_SCOLARITE', 'CHEF_DEPARTEMENT')")
    @Operation(
            summary = "Lister tous les professeurs",
            description = "Retourne la liste de tous les professeurs enregistrés"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des professeurs récupérée avec succès")
    })
    public ResponseEntity<?> getAllProfesseurs() {
        try {
            List<ProfesseurDto> professeurs = professeurService.getAll();
            return ResponseEntity.ok(professeurs);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PROFESSEUR', 'ADMINISTRATEUR', 'SERVICE_SCOLARITE', 'CHEF_DEPARTEMENT')")
    @Operation(
            summary = "Récupérer un professeur par ID",
            description = "Permet de récupérer les informations détaillées d’un professeur via son identifiant"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Professeur trouvé"),
            @ApiResponse(responseCode = "404", description = "Professeur introuvable")
    })
    public ResponseEntity<?> getProfesseurById(@PathVariable Long id) {
        try {
            ProfesseurDto professeur = professeurService.findById(id);
            return ResponseEntity.ok(professeur);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

}