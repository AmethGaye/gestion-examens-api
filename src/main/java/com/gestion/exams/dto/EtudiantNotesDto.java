package com.gestion.exams.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EtudiantNotesDto {
    private Long id;
    private String numeroDossier;
    private String nom;
    private String prenom;
    private List<NoteSimpleDto> notes;
}
