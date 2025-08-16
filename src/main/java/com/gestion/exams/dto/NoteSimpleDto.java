package com.gestion.exams.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteSimpleDto {
    private Long id;
    private Double valeur;
    private Double coefficient;
    private String type;
}
