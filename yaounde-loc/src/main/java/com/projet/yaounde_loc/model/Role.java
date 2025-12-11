package com.projet.yaounde_loc.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) // Stocke le nom de l'Enum (ROLE_...) en String dans la DB
    @Column(nullable = false, unique = true)
    private RoleName name;
}

