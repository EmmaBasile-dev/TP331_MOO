package com.projet.yaounde_loc.controller;

import com.projet.yaounde_loc.dto.AuthResponse;
import com.projet.yaounde_loc.dto.LoginRequest;
import com.projet.yaounde_loc.model.Annonce;
import com.projet.yaounde_loc.model.User;
import com.projet.yaounde_loc.security.JwtTokenProvider;
import com.projet.yaounde_loc.service.UserService;

import java.util.List;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import com.projet.yaounde_loc.service.AnnonceService;

@RestController
@RequestMapping("/annonces")
public class AnnonceController {

    @Autowired
    private AnnonceService annonceService;
    // Ajout d'un moyen pour trouver l'utilisateur

    @Autowired // NOUVEAU : Pour pouvoir trouver le User
    private UserService userService;

   @PostMapping
    public ResponseEntity<Annonce> creerAnnonce(@RequestBody Annonce annonce, Authentication authentication) {
        
        // 1. Récupérer l'email de l'utilisateur connecté (c'est le 'subject' du JWT)
        String emailProprietaire = authentication.getName();
        
        // 2. Trouver l'objet User dans la base de données (CORRECTION DE L'ERREUR)
        User proprietaire = userService.findUserByEmail(emailProprietaire);
        
        // 3. Assigner l'utilisateur (Proprietaire) à l'annonce (Ligne corrigée)
        annonce.setProprietaire(proprietaire); // ⭐ CETTE LIGNE NE DEVRAIT PLUS AVOIR D'ERREUR

        // 4. Sauvegarder l'annonce (le service se charge de mettre le statut EN_ATTENTE)
        Annonce nouvelleAnnonce = annonceService.creerAnnonce(annonce);
        
        return new ResponseEntity<>(nouvelleAnnonce, HttpStatus.CREATED);
    }
    
    // Ajoutez ici la méthode GET pour le test public (si elle n'existe pas)
    @GetMapping
    public ResponseEntity<List<Annonce>> getAllAnnonces() {
        // Supposons que vous avez une méthode getAnnoncesValidees() dans votre service
        List<Annonce> annonces = annonceService.getAnnoncesValidees();
        return ResponseEntity.ok(annonces);
    }

@GetMapping("/my-listings")
    public ResponseEntity<List<Annonce>> getMyAnnonces(Authentication authentication) {
        // Récupère l'utilisateur Propriétaire connecté
        String emailProprietaire = authentication.getName();
        User proprietaire = userService.findUserByEmail(emailProprietaire);
        
        // Récupère TOUTES les annonces de ce propriétaire (Validees, En Attente, Rejetees)
        List<Annonce> annonces = annonceService.getAnnoncesProprietaire(proprietaire);
        return ResponseEntity.ok(annonces);
    }

    // 2. ENDPOINT: PUT /annonces/{id} (Mettre à jour une annonce)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAnnonce(
            @PathVariable Long id, 
            @RequestBody Annonce annonceDetails, 
            Authentication authentication) {
        try {
            String emailProprietaire = authentication.getName();
            User proprietaire = userService.findUserByEmail(emailProprietaire);
            
            Annonce updatedAnnonce = annonceService.updateAnnonce(id, annonceDetails, proprietaire);
            
            // L'annonce est revenue à EN_ATTENTE, on informe l'utilisateur
            return ResponseEntity.ok(updatedAnnonce);
        } catch (RuntimeException e) {
            // Renvoie 403 (Forbidden) si ce n'est pas le propriétaire
            if (e.getMessage().contains("Accès refusé")) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
            }
            // Renvoie 404 (Not Found) si l'annonce n'existe pas
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    // 3. ENDPOINT: DELETE /annonces/{id} (Supprimer une annonce)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnnonce(@PathVariable Long id, Authentication authentication) {
        try {
            String emailProprietaire = authentication.getName();
            User proprietaire = userService.findUserByEmail(emailProprietaire);
            
            annonceService.deleteAnnonce(id, proprietaire);
            
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Accès refusé")) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getAnnonceById(@PathVariable Long id) {
        try {
            Annonce annonce = annonceService.getAnnonceValideeById(id);
            return ResponseEntity.ok(annonce);
        } catch (AccessDeniedException e) {
            // L'annonce est trouvée mais non validée (accès interdit)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN); // 403
        } catch (RuntimeException e) {
            // Annonce non trouvée
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404
        }
    }

   
    @GetMapping("/search")
    public ResponseEntity<List<Annonce>> searchAnnonces(
            @RequestParam(required = false) String quartier,
            @RequestParam(required = false) String typeBien,
            @RequestParam(required = false) Double prixMin,
            @RequestParam(required = false) Double prixMax) {

        
        List<Annonce> annonces = annonceService.rechercherAvancee(quartier, typeBien, prixMin, prixMax);
        return ResponseEntity.ok(annonces);
    }


}
