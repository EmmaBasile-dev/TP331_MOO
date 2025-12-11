package com.projet.yaounde_loc.repository;

import com.projet.yaounde_loc.model.Annonce;
import com.projet.yaounde_loc.model.User;
import com.projet.yaounde_loc.model.StatutAnnonce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnonceRepository extends JpaRepository<Annonce, Long> {

    // Trouver les annonces d’un propriétaire
    List<Annonce> findByProprietaire(User proprietaire);

    // Trouver les annonces selon le statut (Utiliser l'Enum StatutAnnonce)
    List<Annonce> findByStatut(StatutAnnonce statut); 

    // Rechercher par quartier et/ou type de bien
    List<Annonce> findByQuartierContainingIgnoreCaseAndTypeBienContainingIgnoreCase(String quartier, String typeBien);
}

