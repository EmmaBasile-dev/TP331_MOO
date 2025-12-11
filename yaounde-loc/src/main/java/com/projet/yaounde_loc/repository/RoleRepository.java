package com.projet.yaounde_loc.repository;

import com.projet.yaounde_loc.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.projet.yaounde_loc.model.RoleName; 

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
   
    Optional<Role> findByName(RoleName name); 
}
