package com.gestion.exams.service;


import com.gestion.exams.dto.*;
import com.gestion.exams.entity.*;
import com.gestion.exams.mapper.CoursMapper;
import com.gestion.exams.mapper.EtudiantMapper;
import com.gestion.exams.mapper.NoteMapper;
import com.gestion.exams.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private EvaluationRepository evaluationRepository;
    @Autowired
    private ProfesseurRepository professeurRepository;
    @Autowired
    private CoursRepository coursRepository;



    public NoteDto saisirNote(NoteRequestDto request, Long professeurId) {
        // Validation de la note
        if (request.getValeur() < 0 || request.getValeur() > 20) {
            throw new RuntimeException("La note doit être comprise entre 0 et 20");
        }

        // Récupérer les entités
        Etudiant etudiant = etudiantRepository.findById(request.getEtudiantId())
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

        Evaluation evaluation = evaluationRepository.findById(request.getEvaluationId())
                .orElseThrow(() -> new RuntimeException("Évaluation non trouvée"));

        Professeur professeur = professeurRepository.findById(professeurId)
                .orElseThrow(() -> new RuntimeException("Professeur non trouvé"));

        // Vérifier que le professeur enseigne ce cours
        if (!evaluation.getCours().getProfesseur().getId().equals(professeurId)) {
            throw new RuntimeException("Vous n'êtes pas autorisé à saisir des notes pour ce cours");
        }

        // Vérifier s'il existe déjà une note pour cet étudiant et cette évaluation
        Note existingNote = noteRepository.findByEtudiantIdAndEvaluationId(
                request.getEtudiantId(), request.getEvaluationId()).orElse(null);

        Note note;
        if (existingNote != null) {
            // Modifier la note existante
            existingNote.setValeur(request.getValeur());
            existingNote.setDateModification(LocalDateTime.now());
            note = noteRepository.save(existingNote);
        } else {
            // Créer une nouvelle note
            note = new Note();
            note.setValeur(request.getValeur());
            note.setEtudiant(etudiant);
            note.setEvaluation(evaluation);
            note.setProfesseur(professeur);
            note.setDateSaisie(LocalDateTime.now());
            note = noteRepository.save(note);
        }

        return NoteMapper.convertToDto(note);
    }

    public List<NoteDto> getNotesByEvaluation(Long evaluationId) {
        return noteRepository.findByEvaluationId(evaluationId)
                .stream()
                .map(NoteMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public List<NoteDto> getNotesByEtudiant(Long etudiantId) {
        return noteRepository.findByEtudiantId(etudiantId)
                .stream()
                .map(NoteMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public List<NoteDto> getNotesParEtudiantEtCours(Long etudiantId, Long coursId) {
        return noteRepository.findByEtudiantIdAndCoursId(etudiantId, coursId)
                .stream()
                .map(NoteMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public ReleveNoteDto genererReleveNotes(Long etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

        List<Note> notes = noteRepository.findByEtudiantId(etudiantId);
        // Grouper les notes par cours
        List<NoteParCoursDto> notesParcours = notes.stream()
                .collect(Collectors.groupingBy(note -> note.getEvaluation().getCours()))
                .entrySet().stream()
                .map(entry -> {
                    Cours cours = entry.getKey();
                    List<Note> notesForCours = entry.getValue();

                    // Calculer la moyenne du cours
                    double moyenneCours = calculerMoyenneCours(notesForCours);
                    String statut = moyenneCours >= 10 ? "VALIDE" : "ECHOUE";

                    NoteParCoursDto noteParCours = new NoteParCoursDto();
                    noteParCours.setCours(CoursMapper.convertToDto(cours));
                    noteParCours.setNotes(notesForCours.stream()
                            .map(NoteMapper::convertNoteSimpleToDto)
                            .collect(Collectors.toList()));
                    noteParCours.setMoyenneCours(moyenneCours);
                    noteParCours.setStatut(statut);

                    return noteParCours;
                })
                .collect(Collectors.toList());

        // Calculer la moyenne générale
        double moyenneGenerale = notesParcours.stream()
                .mapToDouble(NoteParCoursDto::getMoyenneCours)
                .average()
                .orElse(0.0);

        String mention = calculerMention(moyenneGenerale);

        ReleveNoteDto releveNotes = new ReleveNoteDto();
        releveNotes.setPromotion(etudiant.getPromotion().getAnneeAcademique());
        releveNotes.setEtudiant(EtudiantMapper.convertToDto(etudiant));
        releveNotes.setNotesParCours(notesParcours);
        releveNotes.setMoyenneGenerale(moyenneGenerale);
        releveNotes.setMention(mention);

        return releveNotes;
    }

    private double calculerMoyenneCours(List<Note> notes) {
        if (notes.isEmpty()) return 0.0;

        // Grouper par type d'évaluation et calculer la moyenne pondérée
        double totalNote = 0.0;
        double totalCoefficient = 0.0;

        for (Note note : notes) {
            double coefficient = note.getEvaluation().getCoefficient();
            totalNote += note.getValeur() * coefficient;
            totalCoefficient += coefficient;
        }

        return totalCoefficient > 0 ? totalNote / totalCoefficient : 0.0;
    }

    private String calculerMention(double moyenne) {
        if (moyenne >= 16) return "Très Bien";
        if (moyenne >= 14) return "Bien";
        if (moyenne >= 12) return "Assez Bien";
        if (moyenne >= 10) return "Passable";
        return "Échec";
    }

    public void supprimerNote(Long noteId) {
        if (!noteRepository.existsById(noteId)) {
            throw new RuntimeException("Note non trouvée");
        }
        noteRepository.deleteById(noteId);
    }


}
