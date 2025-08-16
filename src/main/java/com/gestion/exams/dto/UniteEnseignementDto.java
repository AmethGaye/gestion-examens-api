package com.gestion.exams.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniteEnseignementDto {
    private Long id;
    private String codeUE;
    private String titre;
    private String description;
    private Integer credits;
    private Double coefficient;
}
