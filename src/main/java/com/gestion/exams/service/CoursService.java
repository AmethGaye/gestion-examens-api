package com.gestion.exams.service;

import com.gestion.exams.dto.*;
import com.gestion.exams.entity.*;
import com.gestion.exams.mapper.CoursMapper;
import com.gestion.exams.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// ===== SERVICE COURS =====
@Service
@Transactional
public class CoursService {

    @Autowired
    private CoursRepository coursRepository;

//    @Autowired
//    private UniteEnseignementRepository uniteEnseignementRepository;
//    private ProfesseurRepository professeurRepository;
//    private FormationRepository formationRepository;


    public List<CoursDto> findAll() {
        return coursRepository.findAll()
                .stream()
                .map(CoursMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public CoursDto findById(Long id) {
        Cours cours = coursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));
        return CoursMapper.convertToDto(cours);
    }
    public Optional<Cours> coursById(Long id) {
        Optional<Cours> cours = coursRepository.findById(id);
        return cours;
    }

    public List<CoursDto> findByProfesseurId(Long professeurId) {
        return coursRepository.findByProfesseurId(professeurId)
                .stream()
                .map(CoursMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CoursDto> findByFormationId(Long formationId) {
        return coursRepository.findByFormationId(formationId)
                .stream()
                .map(CoursMapper::convertToDto)
                .collect(Collectors.toList());
    }


}