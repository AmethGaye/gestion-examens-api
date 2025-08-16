package com.gestion.exams.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "evaluation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateExamen;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeEvaluation type;

    @ManyToOne
    @JoinColumn(name = "cours_id", nullable = false)
    private Cours cours;

    @Column(nullable = false)
    private Double coefficient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Session session;

    @Column(nullable = false)
    private Boolean estPublie = false;

    @OneToMany(mappedBy = "evaluation")
    private List<Note> notes;

    public Evaluation(LocalDateTime dateExamen, TypeEvaluation type, Cours cours, Double coefficient, Session session, Boolean estPublie) {
        this.dateExamen = dateExamen;
        this.type = type;
        this.cours = cours;
        this.coefficient = coefficient;
        this.session = session;
        this.estPublie = estPublie;
    }
}
