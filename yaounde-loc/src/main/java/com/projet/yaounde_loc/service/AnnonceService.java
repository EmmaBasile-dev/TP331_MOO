package com.projet.yaounde_loc.service;

import com.projet.yaounde_loc.model.Annonce;
import com.projet.yaounde_loc.model.User;
import com.projet.yaounde_loc.model.StatutAnnonce; 
import com.projet.yaounde_loc.repository.AnnonceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.stream.Collectors; // ⭐ NOUVEAU IMPORT (si absent)
import org.springframework.security.access.AccessDeniedException; // ⭐ NOUVEAU IMPORT (si absent)

import java.util.List;

@Service
public class AnnonceService {

    @Autowired
    private AnnonceRepository annonceRepository;

    public Annonce creerAnnonce(Annonce annonce) {
        
        annonce.setStatut(StatutAnnonce.EN_ATTENTE);
        return annonceRepository.save(annonce);
    }

    public List<Annonce> getAnnoncesValidees() {
        
        return annonceRepository.findByStatut(StatutAnnonce.VALIDEE);
    }

    public List<Annonce> rechercher(String quartier, String typeBien) {
        return annonceRepository.findByQuartierContainingIgnoreCaseAndTypeBienContainingIgnoreCase(quartier, typeBien);
    }

    public List<Annonce> getAnnoncesProprietaire(User user) {
        return annonceRepository.findByProprietaire(user);
    }

    public Annonce updateAnnonceStatut(Long annonceId, StatutAnnonce nouveauStatut) {
        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Annonce non trouvée avec l'ID: " + annonceId));

        // On ne devrait changer le statut que si c'est EN_ATTENTE
        if (annonce.getStatut() == StatutAnnonce.EN_ATTENTE) {
            annonce.setStatut(nouveauStatut);
            return annonceRepository.save(annonce);
        } else {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le statut de l'annonce est déjà finalisé.");
        }
    }
    
    // NOUVEAU : Récupérer toutes les annonces en attente (pour le tableau de bord Admin)
    public List<Annonce> getAnnoncesEnAttente() {
        return annonceRepository.findByStatut(StatutAnnonce.EN_ATTENTE);
    }

    public Annonce validerAnnonce(Long annonceId) {
        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée avec l'ID: " + annonceId));
        
        annonce.setStatut(StatutAnnonce.VALIDEE);
        return annonceRepository.save(annonce);
    }


    public Annonce rejeterAnnonce(Long annonceId) {
        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée avec l'ID: " + annonceId));
        
        annonce.setStatut(StatutAnnonce.REJETEE);
        return annonceRepository.save(annonce);
    }


    public Annonce updateAnnonce(Long annonceId, Annonce detailsAnnonce, User proprietaire) {
        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée avec l'ID: " + annonceId));

        // Vérification de sécurité essentielle
        if (!annonce.getProprietaire().getId().equals(proprietaire.getId())) {
             throw new RuntimeException("Accès refusé. Vous n'êtes pas le propriétaire de cette annonce.");
        }
        
        // Mise à jour des champs
        annonce.setTitre(detailsAnnonce.getTitre());
        annonce.setDescription(detailsAnnonce.getDescription());
        annonce.setPrix(detailsAnnonce.getPrix());
        annonce.setQuartier(detailsAnnonce.getQuartier());
        annonce.setTypeBien(detailsAnnonce.getTypeBien());
        annonce.setImageUrl(detailsAnnonce.getImageUrl());
        
        // IMPORTANT: Toute modification remet l'annonce en statut EN_ATTENTE pour re-modération
        annonce.setStatut(StatutAnnonce.EN_ATTENTE); 

        return annonceRepository.save(annonce);
    }
    
    // ⭐ NOUVEAU : Supprimer une annonce
    public void deleteAnnonce(Long annonceId, User proprietaire) {
        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée avec l'ID: " + annonceId));

        // Vérification de sécurité essentielle
        if (!annonce.getProprietaire().getId().equals(proprietaire.getId())) {
             throw new RuntimeException("Accès refusé. Vous n'êtes pas le propriétaire de cette annonce.");
        }
        
        annonceRepository.delete(annonce);
    }

    public Annonce getAnnonceValideeById(Long id) {
        Annonce annonce = annonceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée."));
        
        // Sécurité : Seules les annonces VALIDEE peuvent être consultées publiquement
        if (annonce.getStatut() != StatutAnnonce.VALIDEE) {
            throw new AccessDeniedException("L'annonce n'est pas validée et ne peut pas être consultée publiquement.");
        }
        return annonce;
    }

    
    public List<Annonce> rechercherAvancee(String quartier, String typeBien, Double prixMin, Double prixMax) {
        
        // 1. Commencer par toutes les annonces VALIDEE
        List<Annonce> annoncesFiltrees = annonceRepository.findByStatut(StatutAnnonce.VALIDEE);

        // 2. Appliquer les filtres supplémentaires en utilisant Stream API
        return annoncesFiltrees.stream()
            .filter(a -> quartier == null || quartier.isEmpty() || a.getQuartier().equalsIgnoreCase(quartier))
            .filter(a -> typeBien == null || typeBien.isEmpty() || a.getTypeBien().equalsIgnoreCase(typeBien))
            .filter(a -> prixMin == null || a.getPrix() >= prixMin)
            .filter(a -> prixMax == null || a.getPrix() <= prixMax)
            .collect(Collectors.toList());
    }

}

