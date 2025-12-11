package com.projet.yaounde_loc;

import com.projet.yaounde_loc.model.Role;
import com.projet.yaounde_loc.model.RoleName; 
import com.projet.yaounde_loc.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner; 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean; 
import com.projet.yaounde_loc.service.UserService; 
import org.springframework.security.crypto.password.PasswordEncoder; 

@SpringBootApplication
public class YaoundeLocApplication {

	public static void main(String[] args) {
		SpringApplication.run(YaoundeLocApplication.class, args);
	}


// APRÈS (Correct)
@Bean
public CommandLineRunner initRoles(RoleRepository roleRepository, UserService userService, PasswordEncoder passwordEncoder) {
    return args -> {
        
        if (roleRepository.findByName(RoleName.ROLE_LOCATAIRE).isEmpty()) { 
            roleRepository.save(new Role(null, RoleName.ROLE_LOCATAIRE));
        }
        
        if (roleRepository.findByName(RoleName.ROLE_PROPRIETAIRE).isEmpty()) {
            roleRepository.save(new Role(null, RoleName.ROLE_PROPRIETAIRE));
        }
        
        if (roleRepository.findByName(RoleName.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(new Role(null, RoleName.ROLE_ADMIN));
        }
        
        final String adminEmail = "admin@yaoundeloc.com";
            final String adminPassword = "password";

            // ⚠️ On utilise la méthode Optional pour la vérification
            if (userService.findUserByEmailOptional(adminEmail).isEmpty()) { // 👈 Utilisation de la méthode qui retourne Optional
                // NOTE: La méthode createAdminUser DOIT exister dans UserService.java
                userService.createAdminUser(
                    adminEmail,
                    adminPassword,
                    "Administrateur Principal",
                    "699000000"
                );
                System.out.println("-------------------------------------------------------------------------------------");
                System.out.println("⭐ COMPTE ADMIN CREE : Email: " + adminEmail + " | Mot de passe: " + adminPassword);
                System.out.println("-------------------------------------------------------------------------------------");
            }


    };
}
}