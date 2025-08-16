package com.gestion.exams.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "formation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Formation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String intitule;

    @Column(nullable = false)
    private String niveau;

    @Column(nullable = false)
    private Integer duree;

    @OneToMany(mappedBy = "formation")
    private List<Promotion> promotions;

    @OneToMany(mappedBy = "formation")
    private List<Cours> cours;

    public Formation(String code, String intitule, String niveau, Integer duree) {
        this.code = code;
        this.intitule = intitule;
        this.niveau = niveau;
        this.duree = duree;
    }
}
