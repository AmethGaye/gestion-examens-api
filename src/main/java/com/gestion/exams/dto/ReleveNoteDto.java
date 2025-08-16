package com.gestion.exams.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReleveNoteDto {
    private String promotion;
    private String semestre = "Semestre 1";
    private EtudiantDto etudiant;
    private List<NoteParCoursDto> notesParCours;
    private Double moyenneGenerale;
//    private Integer rang;
    private String mention;
//    private String decision;
}