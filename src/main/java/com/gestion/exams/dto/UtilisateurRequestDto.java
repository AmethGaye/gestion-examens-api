package com.gestion.exams.dto;

import com.gestion.exams.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurRequestDto {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    @NotBlank(message = "Le prenom est obligatoire")
    private String prenom;
    @NotBlank(message = "Le telephone est obligatoire")
    private String telephone;
    @NotBlank(message = "L'email est obligatoire")
    @Email
    private String email;

    private String password;
    @NotNull
    private Role role = Role.ETUDIANT;

    // s'il s'agit d'un etudiant
    private String numeroDossier;
    private LocalDate dateNaissance;

    // s'il s'agit d'un professeur
    private String matricule;
    private String specialite;


}
