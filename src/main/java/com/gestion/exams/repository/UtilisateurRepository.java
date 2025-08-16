package com.gestion.exams.repository;

import com.gestion.exams.entity.Utilisateur;
import com.gestion.exams.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Utilisateur> findByRole(Role role);
    Optional<Utilisateur> findByTelephone(String telephone);
    boolean existsByTelephone(String telephone);
}