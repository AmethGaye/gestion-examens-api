package com.gestion.exams.dto;
import com.gestion.exams.entity.TypeEvaluation;
import com.gestion.exams.entity.Session;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDto {
    private Long id;
    private LocalDateTime dateExamen;
    private TypeEvaluation type;
    private Double coefficient;
    private Session session;
    private Boolean estPublie;
    private CoursDto cours;
}