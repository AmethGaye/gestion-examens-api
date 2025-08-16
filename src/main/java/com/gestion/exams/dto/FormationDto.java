package com.gestion.exams.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormationDto {
    private Long id;
    private String code;
    private String intitule;
    private String niveau;
    private Integer duree;
}