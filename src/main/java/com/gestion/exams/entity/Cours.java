package com.gestion.exams.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "cours")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String titre;

    @ManyToOne
    @JoinColumn(name = "ue_id", nullable = false)
    private UniteEnseignement uniteEnseignement;

    @ManyToOne
    @JoinColumn(name = "professeur_id", nullable = false)
    private Professeur professeur;

    @ManyToOne
    @JoinColumn(name = "formation_id", nullable = false)
    private Formation formation;

    @Column(nullable = false)
    private Integer semestre;

    @OneToMany(mappedBy = "cours")
    private List<Evaluation> evaluations;

    public Cours(String code, String titre, UniteEnseignement uniteEnseignement, Professeur professeur, Formation formation, Integer semestre) {
        this.code = code;
        this.titre = titre;
        this.uniteEnseignement = uniteEnseignement;
        this.professeur = professeur;
        this.formation = formation;
        this.semestre = semestre;
        this.evaluations = evaluations;
    }
}