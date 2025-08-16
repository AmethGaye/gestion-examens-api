package com.gestion.exams.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "professeurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Professeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @Column(nullable = false, unique = true)
    private String matricule;

    @Column(nullable = false)
    private String specialite;

    @OneToMany(mappedBy = "professeur")
    private List<Cours> cours;

    @OneToMany(mappedBy = "professeur")
    private List<Note> notes;

    public Professeur(Utilisateur utilisateur, String matricule, String specialite) {
        this.utilisateur = utilisateur;
        this.matricule = matricule;
        this.specialite = specialite;
        this.cours = cours;
    }
}