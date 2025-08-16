package com.gestion.exams.dto;

import com.gestion.exams.entity.Session;
import com.gestion.exams.entity.TypeEvaluation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationRequestDto {
    private TypeEvaluation type;
    private Double coefficient;
    private LocalDateTime dateExamen;
    private Session session;
    private Boolean estPublie;
    private Long coursId;
}
