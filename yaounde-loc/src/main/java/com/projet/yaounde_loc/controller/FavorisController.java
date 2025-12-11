package com.projet.yaounde_loc.controller;

import com.projet.yaounde_loc.model.Favoris;
import com.projet.yaounde_loc.model.User;
import com.projet.yaounde_loc.service.FavorisService;
import com.projet.yaounde_loc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favoris")
public class FavorisController {

    @Autowired
    private FavorisService favorisService;
    @Autowired
    private UserService userService; // Pour récupérer l'objet User

    // Endpoint pour ajouter une annonce aux favoris
    @PostMapping("/{annonceId}")
    public ResponseEntity<?> ajouterFavori(@PathVariable Long annonceId, Authentication authentication) {
        try {
            User locataire = userService.findUserByEmail(authentication.getName());
            Favoris nouveauFavori = favorisService.ajouterFavori(locataire, annonceId);
            return new ResponseEntity<>(nouveauFavori, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint pour lister les favoris de l'utilisateur
    @GetMapping
    public ResponseEntity<List<Favoris>> listerFavoris(Authentication authentication) {
        User locataire = userService.findUserByEmail(authentication.getName());
        List<Favoris> favoris = favorisService.listerFavoris(locataire);
        return ResponseEntity.ok(favoris);
    }

    // Endpoint pour supprimer un favori (utilisation de l'ID de l'objet Favoris)
    @DeleteMapping("/{favoriId}")
    public ResponseEntity<?> supprimerFavori(@PathVariable Long favoriId, Authentication authentication) {
        try {
            User locataire = userService.findUserByEmail(authentication.getName());
            favorisService.supprimerFavori(favoriId, locataire);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}
