package com.projet.yaounde_loc.controller;

import com.projet.yaounde_loc.dto.AuthResponse;
import com.projet.yaounde_loc.dto.LoginRequest;
import com.projet.yaounde_loc.model.User;
import com.projet.yaounde_loc.security.JwtTokenProvider;
import com.projet.yaounde_loc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider jwtProvider;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Gérer le cas où l'email existe déjà
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/register-proprietaire") // Endpoint pour enregistrer un ROLE_PROPRIETAIRE
    public ResponseEntity<?> registerProprietaire(@RequestBody User user) {
        try {
            User registeredUser = userService.registerProprietaire(user); // Appel à la nouvelle méthode
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // 1. Authentifier l'utilisateur
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            
            // 2. Générer le token JWT
            String token = jwtProvider.generateToken(authentication);

            // 3. Récupérer le rôle principal pour le DTO de réponse
            String role = authentication.getAuthorities().stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Rôle non défini"))
                    .getAuthority();

            // 4. Retourner la réponse sécurisée
            return ResponseEntity.ok(new AuthResponse(token, "Bearer", role));

        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Email ou mot de passe incorrect.", HttpStatus.UNAUTHORIZED);
        } catch (AuthenticationException e) {
             return new ResponseEntity<>("Erreur d'authentification.", HttpStatus.UNAUTHORIZED);
        }
    }
}

