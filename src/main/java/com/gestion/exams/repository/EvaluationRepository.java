package com.gestion.exams.repository;

import com.gestion.exams.entity.Evaluation;
import com.gestion.exams.entity.Session;
import com.gestion.exams.entity.TypeEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByCoursId(Long coursId);
    List<Evaluation> findByCoursIdAndType(Long coursId, TypeEvaluation type);
    List<Evaluation> findByCoursIdAndSession(Long coursId, Session session);

    @Query("SELECT e FROM Evaluation e JOIN e.cours c WHERE c.formation.id = :formationId")
    List<Evaluation> findByFormationId(@Param("formationId") Long formationId);

    @Query("SELECT e FROM Evaluation e WHERE e.estPublie = true AND e.cours.id = :coursId")
    List<Evaluation> findPublishedByCoursId(@Param("coursId") Long coursId);
}
