package com.gestion.exams.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**",  "/error").permitAll() // login et register sans token
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMINISTRATEUR")
                        .requestMatchers("/professeurs/**").hasAnyRole("PROFESSEUR", "ADMINISTRATEUR")
                        .requestMatchers("/etudiants/**").hasAnyRole("ETUDIANT", "PROFESSEUR", "ADMINISTRATEUR", "CHEF_DEPARTEMENT")
                        .requestMatchers("/evaluations/**").hasAnyRole("ETUDIANT", "PROFESSEUR", "ADMINISTRATEUR", "CHEF_DEPARTEMENT")
                        .requestMatchers("/notes/saisir/**").hasAnyRole("PROFESSEUR", "CHEF_DEPARTEMENT")
                        .requestMatchers("/resultats/publier/**").hasAnyRole("SERVICE_SCOLARITE", "ADMINISTRATEUR")
                        .requestMatchers("/cours/**").hasAnyRole("ETUDIANT", "PROFESSEUR", "ADMINISTRATEUR", "CHEF_DEPARTEMENT", "SERVICE_SCOLARITE")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
