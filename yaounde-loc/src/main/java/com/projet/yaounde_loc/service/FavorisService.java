package com.projet.yaounde_loc.service;

import com.projet.yaounde_loc.model.Annonce;
import com.projet.yaounde_loc.model.Favoris;
import com.projet.yaounde_loc.model.User;
import com.projet.yaounde_loc.repository.AnnonceRepository;
import com.projet.yaounde_loc.repository.FavorisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavorisService {

    @Autowired
    private FavorisRepository favorisRepository;
    @Autowired
    private AnnonceRepository annonceRepository; // Pour vérifier l'existence de l'annonce

    // 1. Ajouter une annonce aux favoris
    public Favoris ajouterFavori(User user, Long annonceId) {
        
        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée."));
        
        // Vérifie si l'annonce n'est pas déjà en favoris
        if (favorisRepository.findByUserAndAnnonce(user, annonce).isPresent()) {
            throw new RuntimeException("Cette annonce est déjà dans vos favoris.");
        }

        Favoris favori = Favoris.builder()
                .user(user)
                .annonce(annonce)
                .build();
        
        return favorisRepository.save(favori);
    }
    
    // 2. Supprimer un favori (par l'ID de l'entrée Favoris)
    public void supprimerFavori(Long favoriId, User user) {
        
        // Sécurité : On s'assure que l'utilisateur supprime bien SON favori
        Favoris favori = favorisRepository.findByIdAndUser(favoriId, user)
                .orElseThrow(() -> new RuntimeException("Favori non trouvé ou accès refusé."));

        favorisRepository.delete(favori);
    }

    // 3. Lister les favoris de l'utilisateur
    public List<Favoris> listerFavoris(User user) {
        return favorisRepository.findByUser(user);
    }
}
