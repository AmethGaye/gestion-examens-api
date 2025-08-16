package com.gestion.exams.repository;

import com.gestion.exams.entity.Cours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Long> {
    Optional<Cours> findByCode(String code);
    List<Cours> findByProfesseurId(Long professeurId);
    List<Cours> findByFormationId(Long formationId);
    List<Cours> findByUniteEnseignementId(Long uniteEnseignementId);
    List<Cours> findByFormationIdAndSemestre(Long formationId, Integer semestre);
    boolean existsByCode(String code);
}
