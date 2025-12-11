package com.projet.yaounde_loc.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nom unique du fichier dans le système de stockage (ex: 12345_image.jpg)
    @Column(nullable = false, unique = true)
    private String fileName; 

    // Chemin d'accès relatif (ex: /annonces/12345_image.jpg)
    @Column(nullable = false)
    private String filePath; 
    
    // Type MIME (ex: image/jpeg)
    @Column
    private String fileType; 

    // Lien vers l'annonce
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annonce_id", nullable = false)
    private Annonce annonce; 
}
