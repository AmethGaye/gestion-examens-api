package com.gestion.exams.mapper;

import com.gestion.exams.dto.CoursDto;
import com.gestion.exams.entity.Cours;
import org.springframework.stereotype.Component;

@Component
public class CoursMapper {

    public static CoursDto convertToDto(Cours cours) {
        CoursDto dto = new CoursDto();
        dto.setId(cours.getId());
        dto.setCode(cours.getCode());
        dto.setTitre(cours.getTitre());
        dto.setSemestre(cours.getSemestre());
        dto.setProfesseur(cours.getProfesseur().getUtilisateur().getPrenom()+ " " +
                cours.getProfesseur().getUtilisateur().getNom());
        dto.setCodeUE(cours.getUniteEnseignement().getCodeUE());
        return dto;
    }
}
