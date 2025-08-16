package com.gestion.exams.dto;

import com.gestion.exams.entity.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EtudiantDto {
    private Long id;
    private String numeroDossier;
    private LocalDate dateNaissance;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private Role role;

}
