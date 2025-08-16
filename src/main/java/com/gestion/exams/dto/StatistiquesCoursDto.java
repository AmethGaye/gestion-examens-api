package com.gestion.exams.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatistiquesCoursDto {
    private CoursDto cours;
    private Integer nombreEtudiants;
    private Double moyenneGenerale;
    private Double noteMax;
    private Double noteMin;
    private Double ecartType;
    private Double tauxReussite;
    private Integer nombreAdmis;
    private Integer nombreAjourne;
}