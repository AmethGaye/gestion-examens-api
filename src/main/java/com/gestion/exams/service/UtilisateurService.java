package com.gestion.exams.service;

import com.gestion.exams.dto.RegisterDto;
import com.gestion.exams.dto.UtilisateurDto;
import com.gestion.exams.dto.UtilisateurRequestDto;
import com.gestion.exams.entity.Etudiant;
import com.gestion.exams.entity.Professeur;
import com.gestion.exams.entity.Role;
import com.gestion.exams.entity.Utilisateur;
import com.gestion.exams.mapper.UtilisateurMapper;
import com.gestion.exams.repository.EtudiantRepository;
import com.gestion.exams.repository.ProfesseurRepository;
import com.gestion.exams.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {
    @Autowired
    UtilisateurRepository utilisateurRepository;

    @Autowired
    UtilisateurMapper userMapper;

    @Autowired PromotionService promotionService;

    @Autowired
    EtudiantRepository etudiantRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    ProfesseurRepository professeurRepository;

    @Autowired
    EtudiantService etudiantService;

    @Transactional
    public UtilisateurDto createNewEtudiant(RegisterDto registerDto) {
        Utilisateur newUser = userMapper.registerDTOToUser(registerDto);
        newUser.setMotDePasse(passwordEncoder.encode(registerDto.getPassword()));

        Utilisateur userSaved = utilisateurRepository.save(newUser);

        Etudiant newEtudiant = userMapper.registerDTOToEtudiant(registerDto);
        newEtudiant.setUtilisateur(userSaved);
        newEtudiant.setPromotion(promotionService.getPromotionById(5L));
        etudiantRepository.save(newEtudiant);

        return userMapper.toResponseDto(userSaved);
    }

    @Transactional
    public UtilisateurDto createNewUser(UtilisateurRequestDto utilisateurDto) {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findByEmail(utilisateurDto.getEmail());

        if(utilisateur.isPresent()){
            throw new RuntimeException("l'Email est déjà utilisé");
        }
        Utilisateur newUser = new Utilisateur();
        newUser.setMotDePasse(passwordEncoder.encode(utilisateurDto.getPassword()));
        newUser.setNom(utilisateurDto.getNom());
        newUser.setPrenom(utilisateurDto.getPrenom());
        newUser.setTelephone(utilisateurDto.getTelephone());
        newUser.setEmail(utilisateurDto.getEmail());
        newUser.setRole(utilisateurDto.getRole());
        Utilisateur savedUtilisateur = utilisateurRepository.save(newUser);

        if(utilisateurDto.getRole().equals(Role.ETUDIANT)) {
            Etudiant etudiant = new Etudiant();
            etudiant.setUtilisateur(savedUtilisateur);
            etudiant.setPromotion(promotionService.getPromotionById(5L));
            etudiant.setDateNaissance(utilisateurDto.getDateNaissance());
            etudiant.setNumeroDossier(utilisateurDto.getNumeroDossier());
            etudiantRepository.save(etudiant);
        }

        if(utilisateurDto.getRole().equals(Role.PROFESSEUR)) {
            Professeur professeur = new Professeur();
            professeur.setUtilisateur(savedUtilisateur);
            professeur.setMatricule(utilisateurDto.getMatricule());
            professeur.setSpecialite(utilisateurDto.getSpecialite());
            professeurRepository.save(professeur);
        }

        return userMapper.toResponseDto(savedUtilisateur);
    }

    public UtilisateurDto findByEmail(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        return userMapper.toResponseDto(utilisateur);
    }

//    ------------------------------------------------------------------------

    public UtilisateurDto findById(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return userMapper.toResponseDto(utilisateur);
    }

    public List<UtilisateurDto> findByRole(Role role) {
        return utilisateurRepository.findByRole(role)
                .stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<UtilisateurDto> findAll() {
        return utilisateurRepository.findAll()
                .stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UtilisateurDto updateUser(Long id, UtilisateurRequestDto utilisateurDto) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Vérifier l'unicité de l'email si modifié
        if (!utilisateur.getEmail().equals(utilisateurDto.getEmail()) &&
                utilisateurRepository.existsByEmail(utilisateurDto.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }

        // Vérifier l'unicité du téléphone si modifié
        if (!utilisateur.getTelephone().equals(utilisateurDto.getTelephone()) &&
                utilisateurRepository.existsByTelephone(utilisateurDto.getTelephone())) {
            throw new RuntimeException("Ce numéro de téléphone est déjà utilisé");
        }

        utilisateur.setNom(utilisateurDto.getNom());
        utilisateur.setPrenom(utilisateurDto.getPrenom());
        utilisateur.setTelephone(utilisateurDto.getTelephone());
        utilisateur.setEmail(utilisateurDto.getEmail());
        utilisateur.setRole(utilisateurDto.getRole());
        Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);

        if(utilisateurDto.getRole().equals(Role.ETUDIANT)) {
            Optional<Etudiant> et = etudiantRepository.findByUtilisateurId(utilisateur.getId());
            if (et.isEmpty()) {
                throw new RuntimeException("Etudiant non trouvé");
            }
            Etudiant etudiant = et.get();
            etudiant.setUtilisateur(savedUtilisateur);
            etudiant.setPromotion(promotionService.getPromotionById(5L));
            etudiant.setDateNaissance(utilisateurDto.getDateNaissance());
            etudiant.setNumeroDossier(utilisateurDto.getNumeroDossier());
            etudiantRepository.save(etudiant);
        }

        if(utilisateurDto.getRole().equals(Role.PROFESSEUR)) {
            Optional<Professeur> pr = professeurRepository.findByUtilisateurId(utilisateur.getId());
            if (pr.isEmpty()) {
                throw new RuntimeException("Etudiant non trouvé");
            }
            Professeur professeur = pr.get();
            professeur.setUtilisateur(savedUtilisateur);
            professeur.setMatricule(utilisateurDto.getMatricule());
            professeur.setSpecialite(utilisateurDto.getSpecialite());
            professeurRepository.save(professeur);
        }

        return userMapper.toResponseDto(savedUtilisateur);
    }

    public Boolean deleteUser(Long id) {
        if (!utilisateurRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        // Vérifier s'il est lié à un Professeur
        Optional<Professeur> professeur = professeurRepository.findByUtilisateurId(id);
        professeur.ifPresent(professeurRepository::delete);
        utilisateurRepository.deleteById(id);
        return true;
    }

    public void changerMotDePasse(Long id, String ancienMotDePasse, String nouveauMotDePasse) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(ancienMotDePasse, utilisateur.getMotDePasse())) {
            throw new RuntimeException("Ancien mot de passe incorrect");
        }

        utilisateur.setMotDePasse(passwordEncoder.encode(nouveauMotDePasse));
        utilisateurRepository.save(utilisateur);
    }


}
