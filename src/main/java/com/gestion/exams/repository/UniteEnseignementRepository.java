package com.gestion.exams.repository;

import com.gestion.exams.entity.UniteEnseignement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UniteEnseignementRepository extends JpaRepository<UniteEnseignement, Long> {
    Optional<UniteEnseignement> findByCodeUE(String codeUE);
}