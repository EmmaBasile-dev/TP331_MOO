package com.projet.yaounde_loc.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users")
@Getter // Remplace @Data pour un meilleur contrôle
@Setter // Remplace @Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"roles", "annonces"}) // Exclure les relations de equals/hashCode
public class User {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @ToString.Exclude // Sécurité: Ne jamais afficher le mot de passe dans les logs
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String phone;

    private boolean enabled = true; 

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    
    // Correction de la relation Annonce : un Utilisateur est Propriétaire de plusieurs Annonces
    @OneToMany(mappedBy = "proprietaire", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Annonce> annonces = new HashSet<>();
}

