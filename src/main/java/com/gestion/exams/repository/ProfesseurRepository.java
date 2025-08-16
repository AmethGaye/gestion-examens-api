package com.gestion.exams.repository;

import com.gestion.exams.entity.Professeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfesseurRepository extends JpaRepository<Professeur, Long> {
    Optional<Professeur> findByMatricule(String matricule);
    Optional<Professeur> findByUtilisateurId(Long utilisateurId);
    List<Professeur> findBySpecialite(String specialite);
    boolean existsByMatricule(String matricule);
}