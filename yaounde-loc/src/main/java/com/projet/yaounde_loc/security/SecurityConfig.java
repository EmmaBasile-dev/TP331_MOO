package com.projet.yaounde_loc.security;

import com.projet.yaounde_loc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
  
    

    @Autowired 
    private JwtTokenProvider jwtTokenProvider;

    // Définition des règles d'accès et du filtre JWT
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserService userService) throws Exception {
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(userService, jwtTokenProvider);
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/", "/index.html", "/actuator/health").permitAll()
                    .requestMatchers(HttpMethod.POST, 
                                "/auth/register", 
                                "/auth/register-proprietaire", 
                                "/auth/login"
                        ).permitAll()
                        
                        // Autoriser l'accès aux endpoints d'authentification et à Swagger
                        .requestMatchers("/auth/**", "/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**").permitAll() 
                        .requestMatchers(HttpMethod.GET, "/annonces").permitAll() 
                        .requestMatchers(HttpMethod.GET, "/annonces/{id}").permitAll()   
                        .requestMatchers(HttpMethod.GET, "/annonces/search").permitAll()
                        .requestMatchers(HttpMethod.GET, "/files/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/annonces").hasAuthority("ROLE_PROPRIETAIRE")
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/annonces").hasAuthority("ROLE_PROPRIETAIRE") // Création
                        .requestMatchers(HttpMethod.GET, "/annonces/my-listings").hasAuthority("ROLE_PROPRIETAIRE") // Liste personnelle
                        .requestMatchers(HttpMethod.PUT, "/annonces/{id}").hasAuthority("ROLE_PROPRIETAIRE") // Mise à jour
                        .requestMatchers(HttpMethod.DELETE, "/annonces/{id}").hasAuthority("ROLE_PROPRIETAIRE") // Suppression
                        .requestMatchers(HttpMethod.POST, "/annonces/{annonceId}/upload-image").hasAuthority("ROLE_PROPRIETAIRE")
                        .requestMatchers("/favoris/**").hasAuthority("ROLE_LOCATAIRE")
                        .anyRequest().authenticated()) // Toutes les autres requêtes nécessitent un token
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Pas de sessions HTTP
                .userDetailsService(userService) // Utilise notre UserService pour charger les utilisateurs
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Ajout du filtre JWT

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

