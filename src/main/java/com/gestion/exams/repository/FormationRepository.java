package com.gestion.exams.repository;

import com.gestion.exams.entity.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FormationRepository extends JpaRepository<Formation, Long> {
    Optional<Formation> findByCode(String code);
}