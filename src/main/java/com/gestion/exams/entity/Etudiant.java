package com.gestion.exams.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "etudiants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @Column(nullable = false, unique = true)
    private String numeroDossier;

    @Column(nullable = false)
    private LocalDate dateNaissance;


    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @OneToMany(mappedBy = "etudiant")
    private List<Note> notes;

    public Etudiant(Utilisateur utilisateur, String numeroDossier, LocalDate dateNaissance, Promotion promotion) {
        this.utilisateur = utilisateur;
        this.numeroDossier = numeroDossier;
        this.dateNaissance = dateNaissance;
        this.promotion = promotion;
    }
}