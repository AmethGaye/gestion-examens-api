package com.gestion.exams.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteDto {
    private Long id;
    private Double valeur;
    private String etudiant;
    private String type;
    private String professeur;
    private LocalDateTime dateModification;
}