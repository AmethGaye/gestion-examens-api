package com.gestion.exams.service;

import com.gestion.exams.entity.Role;
import com.gestion.exams.entity.Utilisateur;
import com.gestion.exams.repository.UtilisateurRepository;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class UtilisateurDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurDetailsService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email));

        return new UtilisateurPrincipal(utilisateur);
    }

    // Classe interne pour encapsuler les détails de l'utilisateur
    @Getter
    public static class UtilisateurPrincipal implements UserDetails {
        private final Utilisateur utilisateur;

        public UtilisateurPrincipal(Utilisateur utilisateur) {
            this.utilisateur = utilisateur;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + utilisateur.getRole().name())
            );
        }

        @Override
        public String getPassword() {
            return utilisateur.getMotDePasse();
        }

        @Override
        public String getUsername() {
            return utilisateur.getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        public Long getId() {
            return utilisateur.getId();
        }

        public String getNom() {
            return utilisateur.getNom();
        }

        public String getPrenom() {
            return utilisateur.getPrenom();
        }

        public Role getRole() {
            return utilisateur.getRole();
        }
    }
}
