package com.gestion.exams.repository;

import com.gestion.exams.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Optional<Etudiant> findByUtilisateurId(Long utilisateurId);
    Optional<Etudiant> findByNumeroDossier(String numeroDossier);
    List<Etudiant> findByPromotionId(Long promotionId);
    boolean existsByNumeroDossier(String numeroDossier);
}