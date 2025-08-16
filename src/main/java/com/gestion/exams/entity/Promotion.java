package com.gestion.exams.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "promotion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String anneeAcademique;

    @ManyToOne
    @JoinColumn(name = "formation_id", nullable = false)
    private Formation formation;

    @OneToMany(mappedBy = "promotion")
    private List<Etudiant> etudiants;

    public Promotion(String anneeAcademique, Formation formation) {
        this.anneeAcademique = anneeAcademique;
        this.formation = formation;
    }
}
