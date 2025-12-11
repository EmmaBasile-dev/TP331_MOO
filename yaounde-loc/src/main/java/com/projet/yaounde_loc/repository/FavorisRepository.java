package com.projet.yaounde_loc.repository;

import com.projet.yaounde_loc.model.Annonce;
import com.projet.yaounde_loc.model.Favoris;
import com.projet.yaounde_loc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavorisRepository extends JpaRepository<Favoris, Long> {

    // 1. Trouver tous les favoris pour un utilisateur (pour le listing)
    List<Favoris> findByUser(User user);
    
    // 2. Vérifier si une annonce est déjà en favoris pour un utilisateur (pour ajouter/retirer)
    Optional<Favoris> findByUserAndAnnonce(User user, Annonce annonce);
    
    // 3. Trouver le favori par son ID et l'utilisateur (pour la suppression sécurisée)
    Optional<Favoris> findByIdAndUser(Long id, User user); 
}
