package com.projet.yaounde_loc.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "annonces")
@Data
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
    private String typeBien; // Studio, 2 pièces, etc.

    @Column(nullable = false)
    private String statut = "EN_ATTENTE"; // EN_ATTENTE, VALIDEE, REJETEE

    @Column
    private String imageUrl; // lien vers la photo principale (ou stockage local + URL)

    @Column(nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proprietaire_id")
    private User proprietaire;
}

