package com.gestion.exams.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteRequestDto {
    @NotNull(message = "L'étudiant est obligatoire")
    private Long etudiantId;

    private Long evaluationId;
    private Long professeurId;

    @NotNull(message = "La note est obligatoire")
    @Min(value = 0, message = "La note doit être au moins 0")
    @Max(value = 20, message = "La note ne peut dépasser 20")
    private Double valeur;
}
