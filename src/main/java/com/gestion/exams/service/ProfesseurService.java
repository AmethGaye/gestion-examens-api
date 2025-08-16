package com.gestion.exams.service;

import com.gestion.exams.dto.ProfesseurDto;
import com.gestion.exams.dto.UtilisateurDto;
import com.gestion.exams.entity.Professeur;
import com.gestion.exams.mapper.EtudiantMapper;
import com.gestion.exams.mapper.ProfesseurMapper;
import com.gestion.exams.repository.ProfesseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProfesseurService {

    @Autowired
    private ProfesseurRepository professeurRepository;

    public ProfesseurDto findById(Long id) {
        Professeur professeur = professeurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professeur non trouvé"));
        return ProfesseurMapper.convertToDto(professeur);
    }

    public ProfesseurDto findByUtilisateurId(Long utilisateurId) {
        Professeur professeur = professeurRepository.findByUtilisateurId(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Professeur non trouvé"));
        return ProfesseurMapper.convertToDto(professeur);
    }

    public List<ProfesseurDto> getAll() {
        return professeurRepository.findAll()
                .stream()
                .map(ProfesseurMapper::convertToDto)
                .collect(Collectors.toList());
    }
}