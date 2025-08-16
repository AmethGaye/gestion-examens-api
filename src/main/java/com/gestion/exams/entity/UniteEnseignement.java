package com.gestion.exams.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "unite_enseignement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniteEnseignement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codeUE;

    @Column(nullable = false)
    private String titre;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Integer credits;

    @Column(nullable = false)
    private Double coefficient;

    @OneToMany(mappedBy = "uniteEnseignement")
    private List<Cours> cours;

    public UniteEnseignement(String codeUE, String titre, String description, Integer credits, Double coefficient) {
        this.codeUE = codeUE;
        this.titre = titre;
        this.description = description;
        this.credits = credits;
        this.coefficient = coefficient;
    }
}
