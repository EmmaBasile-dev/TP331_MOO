package com.projet.yaounde_loc.service;

import com.projet.yaounde_loc.model.Role;
import com.projet.yaounde_loc.model.RoleName;
import com.projet.yaounde_loc.model.User;
import com.projet.yaounde_loc.repository.RoleRepository;
import com.projet.yaounde_loc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;






public User registerProprietaire(User user) {
    if (userRepository.existsByEmail(user.getEmail())) {
        throw new RuntimeException("Email déjà utilisé !");
    }

    // ⭐ Changement ici : Recherche du rôle PROPRIETAIRE
    Role userRole = roleRepository.findByName(RoleName.ROLE_PROPRIETAIRE) 
            .orElseThrow(() -> new RuntimeException("Role PROPRIETAIRE non trouvé."));

    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRoles(Collections.singleton(userRole));
    return userRepository.save(user);
}

    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email déjà utilisé !");
        }

        // Utilisation de l'Enum pour trouver le rôle
        Role userRole = roleRepository.findByName(RoleName.ROLE_LOCATAIRE)
                .orElseThrow(() -> new RuntimeException("Role LOCATAIRE non trouvé. Veuillez initialiser la base de données."));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(userRole));
        return userRepository.save(user);
    }
    public Optional<User> findUserByEmailOptional(String email) {
        return userRepository.findByEmail(email);
    }
    
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + email));
    }
    
public User createAdminUser(String email, String password, String fullName, String phone) {
    if (userRepository.existsByEmail(email)) {
        return userRepository.findByEmail(email).get(); // Retourne l'utilisateur s'il existe déjà
    }

    Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
            .orElseThrow(() -> new RuntimeException("Role ADMIN non trouvé."));

    User admin = User.builder()
            .email(email)
            .password(passwordEncoder.encode(password)) 
            .fullName(fullName)
            .phone(phone)
            .enabled(true)
            .roles(Collections.singleton(adminRole))
            .build();

    return userRepository.save(admin);
}



    // Utilisé par Spring Security pour l'authentification
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email));
        
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName().name())).toList()
        );
    }
}

