package com.projet.yaounde_loc.repository;

import com.example.yaoundeloc.model.Annonce;
import com.example.yaoundeloc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnonceRepository extends JpaRepository<Annonce, Long> {

    // Trouver les annonces d’un propriétaire
    List<Annonce> findByProprietaire(User proprietaire);

    // Trouver les annonces selon le statut (ex: VALIDEE)
    List<Annonce> findByStatut(String statut);

    // Rechercher par quartier et/ou type de bien
    List<Annonce> findByQuartierContainingIgnoreCaseAndTypeBienContainingIgnoreCase(String quartier, String typeBien);
}

