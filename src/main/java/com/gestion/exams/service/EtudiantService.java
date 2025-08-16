package com.gestion.exams.service;

import com.gestion.exams.dto.*;
import com.gestion.exams.entity.*;
import com.gestion.exams.mapper.CoursMapper;
import com.gestion.exams.mapper.EtudiantMapper;
import com.gestion.exams.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EtudiantService {
    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    EtudiantMapper etudiantMapper;



    public Etudiant findById(Long id) {
        return etudiantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
    }

    public Optional<Etudiant> findByUtilisateurId(Long utilisateurId) {
        return etudiantRepository.findByUtilisateurId(utilisateurId);
    }


    public List<EtudiantDto> findByPromotionId(Long promotionId) {
        return etudiantRepository.findByPromotionId(promotionId)
                .stream()
                .map(EtudiantMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EtudiantDto> getAllEtudiants() {
        return etudiantRepository.findAll()
                .stream()
                .map(EtudiantMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<Etudiant> getEtudiantById(Long id) {
        return etudiantRepository.findById(id);
    }



}