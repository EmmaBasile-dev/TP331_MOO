package com.projet.yaounde_loc.repository;

import com.example.yaoundeloc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Trouver un utilisateur par email (utile pour la connexion)
    Optional<User> findByEmail(String email);

    // Vérifier si un email existe déjà (pour empêcher les doublons)
    boolean existsByEmail(String email);
}

