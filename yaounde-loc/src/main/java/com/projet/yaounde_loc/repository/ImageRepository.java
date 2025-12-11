package com.projet.yaounde_loc.repository;

import com.projet.yaounde_loc.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    // Utile pour retrouver un fichier par son nom avant de le servir
    Optional<Image> findByFileName(String fileName); 
}
