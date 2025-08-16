package com.gestion.exams.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteParCoursDto {
    private CoursDto cours;
    private List<NoteSimpleDto> notes;
    private Double moyenneCours;
    private String statut;


}
