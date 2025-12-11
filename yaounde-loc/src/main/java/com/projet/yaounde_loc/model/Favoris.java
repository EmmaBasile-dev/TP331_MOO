package com.projet.yaounde_loc.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "favoris")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Assurez-vous d'avoir Lombok configuré pour @Getter, @Setter, etc.
// On peut ajouter une contrainte d'unicité (user_id, annonce_id) pour éviter les doublons si nécessaire.
// @Table(name = "favoris", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "annonce_id"})) 
public class Favoris {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Le locataire qui a mis en favori

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annonce_id", nullable = false)
    private Annonce annonce; // L'annonce mise en favori
    
    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime dateAjout = LocalDateTime.now();

    
}
