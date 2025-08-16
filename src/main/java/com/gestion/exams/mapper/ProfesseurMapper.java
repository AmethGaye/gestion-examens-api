package com.gestion.exams.mapper;

import com.gestion.exams.dto.ProfesseurDto;
import com.gestion.exams.dto.UtilisateurDto;
import com.gestion.exams.entity.Professeur;
import org.springframework.stereotype.Component;

@Component
public class ProfesseurMapper {
    public static ProfesseurDto convertToDto(Professeur professeur) {
        ProfesseurDto dto = new ProfesseurDto();
        dto.setId(professeur.getId());
        dto.setMatricule(professeur.getMatricule());
        dto.setSpecialite(professeur.getSpecialite());
        dto.setNom(professeur.getUtilisateur().getNom());
        dto.setPrenom(professeur.getUtilisateur().getPrenom());
        dto.setEmail(professeur.getUtilisateur().getEmail());
        dto.setRole(professeur.getUtilisateur().getRole());
        return dto;
    }
}
