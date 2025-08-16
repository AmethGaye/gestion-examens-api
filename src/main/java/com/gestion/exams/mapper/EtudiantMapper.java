package com.gestion.exams.mapper;

import com.gestion.exams.dto.EtudiantDto;
import com.gestion.exams.entity.Etudiant;
import org.springframework.stereotype.Component;

@Component
public class EtudiantMapper {

    public static EtudiantDto convertToDto(Etudiant etudiant) {
        EtudiantDto et = new EtudiantDto();
        et.setId(etudiant.getId());
        et.setNumeroDossier(etudiant.getNumeroDossier());
        et.setDateNaissance(etudiant.getDateNaissance());
        et.setNom(etudiant.getUtilisateur().getNom());
        et.setPrenom(etudiant.getUtilisateur().getPrenom());
        et.setEmail(etudiant.getUtilisateur().getEmail());
        et.setTelephone(etudiant.getUtilisateur().getTelephone());
        et.setRole(etudiant.getUtilisateur().getRole());
        return et;
    }

}
