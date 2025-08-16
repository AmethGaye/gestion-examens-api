package com.gestion.exams.mapper;

import com.gestion.exams.dto.NoteDto;
import com.gestion.exams.dto.NoteSimpleDto;
import com.gestion.exams.entity.Note;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {
    public static NoteDto convertToDto(Note note) {
        NoteDto dto = new NoteDto();
        dto.setId(note.getId());
        dto.setValeur(note.getValeur());
        dto.setEtudiant(note.getEtudiant().getUtilisateur().getPrenom() +" "+
                note.getEtudiant().getUtilisateur().getNom());
        dto.setType(String.valueOf(note.getEvaluation().getType()));
        dto.setProfesseur(note.getProfesseur().getUtilisateur().getPrenom() +" "+
                note.getProfesseur().getUtilisateur().getNom());
        dto.setDateModification(note.getDateModification());
        return dto;
    }

    public static NoteSimpleDto convertNoteSimpleToDto(Note note) {
        NoteSimpleDto dto = new NoteSimpleDto();
        dto.setId(note.getId());
        dto.setValeur(note.getValeur());
        dto.setCoefficient(note.getEvaluation().getCoefficient());
        dto.setType(String.valueOf(note.getEvaluation().getType()));
        return dto;
    }
}
