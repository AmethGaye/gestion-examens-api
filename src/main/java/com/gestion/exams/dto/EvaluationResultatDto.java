package com.gestion.exams.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationResultatDto {
    private Long id;
    private String coursCode;
    private String coursTitre;
    private String typeEvaluation;
    private Double coefficient;
    private List<NoteRequestDto> notes;
    private Double moyenneClasse;
    private Double noteMax;
    private Double noteMin;
}