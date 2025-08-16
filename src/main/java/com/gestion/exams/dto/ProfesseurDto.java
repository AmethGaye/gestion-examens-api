package com.gestion.exams.dto;

import com.gestion.exams.entity.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfesseurDto {
    private Long id;
    private String matricule;
    private String specialite;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private Role role;
}

