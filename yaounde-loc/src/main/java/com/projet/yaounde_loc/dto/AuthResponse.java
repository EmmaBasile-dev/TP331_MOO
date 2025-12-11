package com.projet.yaounde_loc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private String role; // Ajout du rôle pour faciliter le frontend
}
