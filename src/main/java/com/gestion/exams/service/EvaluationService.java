package com.gestion.exams.service;

import com.gestion.exams.dto.*;
import com.gestion.exams.entity.*;
import com.gestion.exams.mapper.EvaluationMapper;
import com.gestion.exams.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;
    @Autowired
    private CoursRepository coursRepository;
    @Autowired
    private NoteRepository noteRepository;

    public List<EvaluationDto> findAll() {
        return evaluationRepository.findAll()
                .stream()
                .map(EvaluationMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public EvaluationDto findById(Long id) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Évaluation non trouvée"));
        return EvaluationMapper.convertToDto(evaluation);
    }

    public List<EvaluationDto> findByCoursId(Long coursId) {
        return evaluationRepository.findByCoursId(coursId)
                .stream()
                .map(EvaluationMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EvaluationDto> findByFormationId(Long formationId) {
        return evaluationRepository.findByFormationId(formationId)
                .stream()
                .map(EvaluationMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EvaluationDto> findPublishedByCoursId(Long coursId) {
        return evaluationRepository.findPublishedByCoursId(coursId)
                .stream()
                .map(EvaluationMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public void publierResultats(Long evaluationId) {
        Evaluation evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new RuntimeException("Évaluation non trouvée"));

        evaluation.setEstPublie(true);
        evaluationRepository.save(evaluation);
    }

    public Evaluation saveEvaluation(Evaluation evaluation) {
        return evaluationRepository.save(evaluation);
    }

    public Optional<Evaluation> getEvaluationById(Long id) {
        return evaluationRepository.findById(id);
    }
}
