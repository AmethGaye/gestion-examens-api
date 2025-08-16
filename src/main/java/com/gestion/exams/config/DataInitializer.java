package com.gestion.exams.config;

import com.gestion.exams.entity.*;
import com.gestion.exams.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Autowired
    private UniteEnseignementRepository uniteEnseignementRepository;

    @Autowired
    private CoursRepository coursRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (utilisateurRepository.count() == 0) {
            System.out.println("🚀 Initialisation des données de gestion d'examens...");
            initializeData();
            System.out.println("✅ Données initialisées avec succès !");
        } else {
            System.out.println("📊 Données déjà présentes en base");
        }
    }

    private void initializeData() {
        // 1. Créer les utilisateurs
        createUtilisateurs();

        // 2. Créer la formation
        Formation formation = createFormation();

        // 3. Créer la promotion
        Promotion promotion = createPromotion(formation);

        // 4. Créer les étudiants
        createEtudiants(promotion);

        // 5. Créer les professeurs
        createProfesseurs();

        // 6. Créer les unités d'enseignement
        createUniteEnseignement();

        // 7. Créer les cours
        createCours(formation);

        // 8. Créer les évaluations
        createEvaluations();

        // 9. Créer les notes
        createNotes();
    }

    private void createUtilisateurs() {
        // Mot de passe commun (à hasher en production !)
        String motDePasseCommun = "$2a$10$IOaCXtxcmpX1oaqRbM/kN.3IpTFlOqgIKOm9AMJgcoO5baCmuRunO";

        // Étudiants - Utilisation du constructeur sans ID
        Utilisateur[] etudiants = {
                new Utilisateur("Ba", "Fatou", "+221 77 5614226", "fatou.ba@univ-thies.sn", motDePasseCommun, Role.ETUDIANT),
                new Utilisateur("Fall", "Aminata", "+221 77 2719583", "aminata.fall@univ-thies.sn", motDePasseCommun, Role.ETUDIANT),
                new Utilisateur("Diop", "Ibrahima", "+221 77 8078673", "ibrahima.diop@univ-thies.sn", motDePasseCommun, Role.ETUDIANT),
                new Utilisateur("Diop", "Amadou", "+221 77 4668136", "amadou.diop@univ-thies.sn", motDePasseCommun, Role.ETUDIANT),
                new Utilisateur("Cisse", "Ibrahima", "+221 77 1445199", "ibrahima.cisse@univ-thies.sn", motDePasseCommun, Role.ETUDIANT),
                new Utilisateur("Kane", "Aminata", "+221 77 8038374", "aminata.kane@univ-thies.sn", motDePasseCommun, Role.ETUDIANT),
                new Utilisateur("Cisse", "Khady", "+221 77 5667265", "khady.cisse@univ-thies.sn", motDePasseCommun, Role.ETUDIANT),
                new Utilisateur("Sy", "Moussa", "+221 77 6708456", "moussa.sy@univ-thies.sn", motDePasseCommun, Role.ETUDIANT),
                new Utilisateur("Gueye", "Moussa", "+221 77 6647119", "moussa.gueye@univ-thies.sn", motDePasseCommun, Role.ETUDIANT),
                new Utilisateur("Sy", "Fatou", "+221 77 2622631", "fatou.sy@univ-thies.sn", motDePasseCommun, Role.ETUDIANT)
        };

        for (Utilisateur etudiant : etudiants) {
            utilisateurRepository.save(etudiant);
        }

        // Professeurs - Utilisation du constructeur sans ID
        Utilisateur[] professeurs = {
                new Utilisateur("Cisse", "Ndeye", "+221 77 5437923", "ndeye.cisse@univ-thies.sn", motDePasseCommun, Role.PROFESSEUR),
                new Utilisateur("Kane", "Khady", "+221 77 3094235", "khady.kane@univ-thies.sn", motDePasseCommun, Role.PROFESSEUR),
                new Utilisateur("Kane", "Fatou", "+221 77 5918715", "fatou.kane@univ-thies.sn", motDePasseCommun, Role.PROFESSEUR),
                new Utilisateur("Cisse", "Ndeye", "+221 77 4226067", "ndeye.cisse2@univ-thies.sn", motDePasseCommun, Role.PROFESSEUR),
                new Utilisateur("Gueye", "Amadou", "+221 77 5855124", "amadou.gueye@univ-thies.sn", motDePasseCommun, Role.PROFESSEUR),
                new Utilisateur("Diop", "Aminata", "+221 77 7377459", "aminata.diop@univ-thies.sn", motDePasseCommun, Role.PROFESSEUR),
                new Utilisateur("Sarr", "Khady", "+221 77 3728882", "khady.sarr@univ-thies.sn", motDePasseCommun, Role.PROFESSEUR),
                new Utilisateur("Gueye", "Ndeye", "+221 77 5479144", "ndeye.gueye@univ-thies.sn", motDePasseCommun, Role.PROFESSEUR),
                new Utilisateur("Fall", "Seynabou", "+221 77 9961380", "seynabou.fall@univ-thies.sn", motDePasseCommun, Role.PROFESSEUR),
                new Utilisateur("Faye", "Moussa", "+221 77 7366205", "moussa.faye@univ-thies.sn", motDePasseCommun, Role.PROFESSEUR)
        };

        for (Utilisateur professeur : professeurs) {
            utilisateurRepository.save(professeur);
        }

        // Administrateur - Utilisation du constructeur sans ID
        Utilisateur admin = new Utilisateur("Admin", "System", "+221 77 0000000", "admin@univ-thies.sn", motDePasseCommun, Role.ADMINISTRATEUR);
        utilisateurRepository.save(admin);
    }

    private Formation createFormation() {
        Formation formation = new Formation("GL-MASTER", "Master en Genie Logiciel", "Master", 2);
        return formationRepository.save(formation);
    }

    private Promotion createPromotion(Formation formation) {
        Promotion promotion = new Promotion("2024-2025", formation);
        return promotionRepository.save(promotion);
    }

    private void createEtudiants(Promotion promotion) {
        String[] numerosDossier = {
                "2024MAS001", "2024MAS002", "2024MAS003", "2024MAS004", "2024MAS005",
                "2024MAS006", "2024MAS007", "2024MAS008", "2024MAS009", "2024MAS010"
        };

        LocalDate[] datesNaissance = {
                LocalDate.of(2001, 5, 16), LocalDate.of(2003, 10, 17), LocalDate.of(2000, 3, 6),
                LocalDate.of(2001, 4, 21), LocalDate.of(2003, 2, 23), LocalDate.of(2001, 3, 27),
                LocalDate.of(2000, 1, 14), LocalDate.of(2001, 7, 23), LocalDate.of(2000, 7, 28),
                LocalDate.of(2002, 1, 5)
        };

        for (int i = 0; i < 10; i++) {
            Utilisateur utilisateur = utilisateurRepository.findByEmail(
                    switch(i) {
                        case 0 -> "fatou.ba@univ-thies.sn";
                        case 1 -> "aminata.fall@univ-thies.sn";
                        case 2 -> "ibrahima.diop@univ-thies.sn";
                        case 3 -> "amadou.diop@univ-thies.sn";
                        case 4 -> "ibrahima.cisse@univ-thies.sn";
                        case 5 -> "aminata.kane@univ-thies.sn";
                        case 6 -> "khady.cisse@univ-thies.sn";
                        case 7 -> "moussa.sy@univ-thies.sn";
                        case 8 -> "moussa.gueye@univ-thies.sn";
                        case 9 -> "fatou.sy@univ-thies.sn";
                        default -> throw new IllegalStateException("Index invalide: " + i);
                    }
            ).orElseThrow();

            Etudiant etudiant = new Etudiant(utilisateur, numerosDossier[i], datesNaissance[i], promotion);
            etudiantRepository.save(etudiant);
        }
    }

    private void createProfesseurs() {
        String[] matricules = {"PROF001", "PROF002", "PROF003", "PROF004", "PROF005",
                "PROF006", "PROF007", "PROF008", "PROF009", "PROF010"};
        String[] specialites = {"Algo", "GL", "Web", "Algo", "Algo",
                "IA", "IA", "Algo", "Base de donnees", "IA"};
        String[] emails = {
                "ndeye.cisse@univ-thies.sn", "khady.kane@univ-thies.sn", "fatou.kane@univ-thies.sn",
                "ndeye.cisse2@univ-thies.sn", "amadou.gueye@univ-thies.sn", "aminata.diop@univ-thies.sn",
                "khady.sarr@univ-thies.sn", "ndeye.gueye@univ-thies.sn", "seynabou.fall@univ-thies.sn",
                "moussa.faye@univ-thies.sn"
        };

        for (int i = 0; i < 10; i++) {
            Utilisateur utilisateur = utilisateurRepository.findByEmail(emails[i]).orElseThrow();
            Professeur professeur = new Professeur(utilisateur, matricules[i], specialites[i]);
            professeurRepository.save(professeur);
        }
    }

    private void createUniteEnseignement() {
        UniteEnseignement[] ues = {
                new UniteEnseignement("INF411", "Mathematiques", "Probabilites, Graphes", 6, 2.0),
                new UniteEnseignement("INF412", "Systemes et Securite", "Crypto, Reseaux", 6, 2.0),
                new UniteEnseignement("INF413", "Informatique", "SI, TCP/IP", 6, 2.0),
                new UniteEnseignement("INF414", "Web et IA", "Services Web, IA", 6, 2.0),
                new UniteEnseignement("INF415", "Humanites", "Anglais, TIC, Com", 6, 2.0)
        };

        for (UniteEnseignement ue : ues) {
            uniteEnseignementRepository.save(ue);
        }
    }

    private void createCours(Formation formation) {
        // Récupérer les UE et professeurs
        UniteEnseignement ue1 = uniteEnseignementRepository.findByCodeUE("INF411").orElseThrow();
        UniteEnseignement ue2 = uniteEnseignementRepository.findByCodeUE("INF412").orElseThrow();
        UniteEnseignement ue3 = uniteEnseignementRepository.findByCodeUE("INF413").orElseThrow();
        UniteEnseignement ue4 = uniteEnseignementRepository.findByCodeUE("INF414").orElseThrow();
        UniteEnseignement ue5 = uniteEnseignementRepository.findByCodeUE("INF415").orElseThrow();

        Professeur prof1 = professeurRepository.findByMatricule("PROF001").orElseThrow();
        Professeur prof2 = professeurRepository.findByMatricule("PROF002").orElseThrow();
        Professeur prof4 = professeurRepository.findByMatricule("PROF004").orElseThrow();
        Professeur prof5 = professeurRepository.findByMatricule("PROF005").orElseThrow();
        Professeur prof6 = professeurRepository.findByMatricule("PROF006").orElseThrow();
        Professeur prof7 = professeurRepository.findByMatricule("PROF007").orElseThrow();
        Professeur prof9 = professeurRepository.findByMatricule("PROF009").orElseThrow();

        Cours[] cours = {
                new Cours("INF41\1-1", "Probabilites", ue1, prof9, formation, 1),
                new Cours("INF411-2", "Algorithmes Graphes", ue1, prof4, formation, 1),
                new Cours("INF412-3", "Cryptographie", ue2, prof6, formation, 1),
                new Cours("INF412-4", "Admin Reseaux", ue2, prof1, formation, 1),
                new Cours("INF413-5", "Systemes Info", ue3, prof4, formation, 1),
                new Cours("INF413-6", "TCP/IP", ue3, prof1, formation, 1),
                new Cours("INF414-7", "Services Web", ue4, prof6, formation, 1),
                new Cours("INF414-8", "IA", ue4, prof7, formation, 1),
                new Cours("INF415-9", "Anglais", ue5, prof5, formation, 1),
                new Cours("INF415-10", "TICs", ue5, prof2, formation, 1),
                new Cours("INF415-11", "Communication", ue5, prof4, formation, 1)
        };

        for (Cours c : cours) {
            coursRepository.save(c);
        }
    }

    private void createEvaluations() {
        // Données des évaluations
        Object[][] evaluationsData = {
                {LocalDateTime.of(2025, 1, 11, 0, 0), TypeEvaluation.DEVOIR, "INF411-1", 0.4},
                {LocalDateTime.of(2025, 1, 12, 0, 0), TypeEvaluation.EXAMEN, "INF411-1", 0.6},
                {LocalDateTime.of(2025, 1, 13, 0, 0), TypeEvaluation.DEVOIR, "INF411-2", 0.4},
                {LocalDateTime.of(2025, 1, 14, 0, 0), TypeEvaluation.EXAMEN, "INF411-2", 0.6},
                {LocalDateTime.of(2025, 1, 15, 0, 0), TypeEvaluation.DEVOIR, "INF412-3", 0.4},
                {LocalDateTime.of(2025, 1, 16, 0, 0), TypeEvaluation.EXAMEN, "INF412-3", 0.6},
                {LocalDateTime.of(2025, 1, 17, 0, 0), TypeEvaluation.EXAMEN, "INF412-4", 1.0},
                {LocalDateTime.of(2025, 1, 18, 0, 0), TypeEvaluation.DEVOIR, "INF413-5", 0.4},
                {LocalDateTime.of(2025, 1, 19, 0, 0), TypeEvaluation.EXAMEN, "INF413-5", 0.6},
                {LocalDateTime.of(2025, 1, 20, 0, 0), TypeEvaluation.EXAMEN, "INF413-6", 1.0},
                {LocalDateTime.of(2025, 1, 21, 0, 0), TypeEvaluation.DEVOIR, "INF414-7", 0.4},
                {LocalDateTime.of(2025, 1, 22, 0, 0), TypeEvaluation.EXAMEN, "INF414-7", 0.6},
                {LocalDateTime.of(2025, 1, 23, 0, 0), TypeEvaluation.EXAMEN, "INF414-8", 1.0},
                {LocalDateTime.of(2025, 1, 24, 0, 0), TypeEvaluation.EXAMEN, "INF415-9", 1.0},
                {LocalDateTime.of(2025, 1, 25, 0, 0), TypeEvaluation.EXAMEN, "INF415-10", 1.0},
                {LocalDateTime.of(2025, 1, 26, 0, 0), TypeEvaluation.DEVOIR, "INF415-11", 0.4},
                {LocalDateTime.of(2025, 1, 27, 0, 0), TypeEvaluation.EXAMEN, "INF415-11", 0.6}
        };

        int evaluationsCreated = 0;
        for (Object[] data : evaluationsData) {
            try {
                Optional<Cours> coursOpt = coursRepository.findByCode((String) data[2]);

                if (coursOpt.isPresent()) {
                    Evaluation evaluation = new Evaluation(
                            (LocalDateTime) data[0],
                            (TypeEvaluation) data[1],
                            coursOpt.get(),
                            (Double) data[3],
                            Session.NORMALE,
                            true
                    );
                    evaluationRepository.save(evaluation);
                    evaluationsCreated++;
                    System.out.println("Évaluation créée pour le cours: " + data[2]);
                } else {
                    System.err.println("Cours non trouvé pour l'évaluation: " + data[2]);
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la création de l'évaluation pour: " + data[2]);
                e.printStackTrace();
            }
        }

        System.out.println("📋 " + evaluationsCreated + " évaluations créées sur " + evaluationsData.length + " prévues");
    }

    private void createNotes() {
        // Recherche sécurisée des premières évaluations
        if (evaluationRepository.count() < 2) {
            System.err.println("Pas assez d'évaluations pour créer les notes");
            return;
        }

        // Récupérer les 2 premières évaluations (Probabilités)
        var evaluations = evaluationRepository.findAll();
        if (evaluations.size() < 2) {
            System.err.println("Impossible de récupérer les évaluations");
            return;
        }

        Evaluation eval1 = evaluations.get(0);
        Evaluation eval2 = evaluations.get(1);

        // Récupérer le professeur PROF009
        Optional<Professeur> prof9Opt = professeurRepository.findByMatricule("PROF009");
        if (prof9Opt.isEmpty()) {
            System.err.println("Professeur PROF009 non trouvé");
            return;
        }
        Professeur prof9 = prof9Opt.get();

        // Notes pour les évaluations
        Double[] notesEval1 = {10.39, 7.4, 6.32, 17.99, 15.06, 12.55, 5.28, 12.66, 15.76, 16.75};
        Double[] notesEval2 = {7.53, 5.38, 7.29, 17.98, 15.17, 17.83, 10.16, 12.07, 16.32, 16.09};

        var etudiants = etudiantRepository.findAll();
        int notesCreated = 0;

        for (int i = 0; i < Math.min(etudiants.size(), notesEval1.length); i++) {
            try {
                Etudiant etudiant = etudiants.get(i);

                // Note pour l'évaluation 1
                Note note1 = new Note(notesEval1[i], etudiant, eval1, prof9);
                noteRepository.save(note1);

                // Note pour l'évaluation 2
                Note note2 = new Note(notesEval2[i], etudiant, eval2, prof9);
                noteRepository.save(note2);

                notesCreated += 2;
                System.out.println("Notes créées pour l'étudiant: " + etudiant.getUtilisateur().getPrenom() + " " + etudiant.getUtilisateur().getNom());

            } catch (Exception e) {
                System.err.println("Erreur lors de la création des notes pour l'étudiant " + (i+1));
                e.printStackTrace();
            }
        }

        System.out.println(notesCreated + " notes créées");
    }
}
