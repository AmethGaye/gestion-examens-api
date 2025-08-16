package com.gestion.exams.mapper;

import com.gestion.exams.entity.*;
import com.gestion.exams.dto.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

// 1. MAPPER POUR UTILISATEUR
@Component
public class UtilisateurMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Utilisateur registerDTOToUser(@Valid RegisterDto registerDTO) {
        Utilisateur user = new Utilisateur();
        user.setNom(registerDTO.getNom());
        user.setPrenom(registerDTO.getPrenom());
        user.setEmail(registerDTO.getEmail());
        user.setTelephone(registerDTO.getTelephone());
        user.setRole(registerDTO.getRole());
        return user;
    }

    public Etudiant registerDTOToEtudiant(@Valid RegisterDto registerDTO) {
        Etudiant et = new Etudiant();
        et.setNumeroDossier(registerDTO.getNumeroDossier());
        et.setDateNaissance(registerDTO.getDateNaissance());
        return et;
    }

//    public Professeur registerDTOToProfesseur(@Valid RegisterDto registerDTO) {
//        Professeur pr = new Professeur();
//        pr.setMatricule(registerDTO.getMatricule());
//        pr.setSpecialite(registerDTO.getSpecialite());
//        return pr;
//    }

    // Entité vers DTO Response
    public UtilisateurDto toResponseDto(Utilisateur utilisateur) {
        if (utilisateur == null) return null;

        return new UtilisateurDto(
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getTelephone(),
                utilisateur.getEmail(),
                utilisateur.getRole()
        );
    }

    // Liste d'entités vers liste de DTOs
    public List<UtilisateurDto> toResponseDtoList(List<Utilisateur> utilisateurs) {
        return utilisateurs.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}
