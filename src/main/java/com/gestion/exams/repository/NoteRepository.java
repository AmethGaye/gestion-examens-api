package com.gestion.exams.repository;

import com.gestion.exams.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByEtudiantId(Long etudiantId);
    List<Note> findByEvaluationId(Long evaluationId);
    List<Note> findByProfesseurId(Long professeurId);

    Optional<Note> findByEtudiantIdAndEvaluationId(Long etudiantId, Long evaluationId);

    @Query("SELECT n FROM Note n WHERE n.etudiant.id = :etudiantId AND n.evaluation.cours.id = :coursId")
    List<Note> findByEtudiantIdAndCoursId(@Param("etudiantId") Long etudiantId, @Param("coursId") Long coursId);

    @Query("SELECT n FROM Note n WHERE n.evaluation.cours.id = :coursId AND n.evaluation.id = :evaluationId")
    List<Note> findByCoursIdAndEvaluationId(@Param("coursId") Long coursId, @Param("evaluationId") Long evaluationId);

    @Query("SELECT COUNT(n) FROM Note n WHERE n.evaluation.id = :evaluationId")
    Long countByEvaluationId(@Param("evaluationId") Long evaluationId);
}