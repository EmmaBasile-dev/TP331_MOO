package com.projet.yaounde_loc.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

// L'énumération StatutAnnonce interne a été supprimée, 
// et la classe utilise maintenant l'énumération du fichier séparé.

@Entity
@Table(name = "annonces")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Annonce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private double prix;

    @Column(nullable = false)
    private String quartier;

    @Column(nullable = false)
    private String typeBien; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutAnnonce statut = StatutAnnonce.EN_ATTENTE; // Utilisation de l'Enum externe

    @Column
    private String imageUrl; 

    @Column(nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proprietaire_id")
    private User proprietaire; // Lien vers le propriétaire (User)

    @OneToMany(mappedBy = "annonce", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Image> images = new HashSet<>();
}

