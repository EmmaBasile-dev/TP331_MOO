package com.projet.yaounde_loc.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // Exemple: "ROLE_ADMIN", "ROLE_PROPRIETAIRE", "ROLE_LOCATAIRE"
}

