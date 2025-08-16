package com.gestion.exams.mapper;

import com.gestion.exams.dto.EvaluationDto;
import com.gestion.exams.entity.Evaluation;

public class EvaluationMapper {
    public static EvaluationDto convertToDto(Evaluation evaluation) {
        EvaluationDto dto = new EvaluationDto();
        dto.setId(evaluation.getId());
        dto.setDateExamen(evaluation.getDateExamen());
        dto.setType(evaluation.getType());
        dto.setCoefficient(evaluation.getCoefficient());
        dto.setSession(evaluation.getSession());
        dto.setEstPublie(evaluation.getEstPublie());
        dto.setCours(CoursMapper.convertToDto(evaluation.getCours()));
        return dto;
    }
}
