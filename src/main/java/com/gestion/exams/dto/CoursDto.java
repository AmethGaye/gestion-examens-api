package com.gestion.exams.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoursDto {
    private Long id;
    private String code;
    private String titre;
    private Integer semestre;
    private String professeur;
    private String codeUE;
}