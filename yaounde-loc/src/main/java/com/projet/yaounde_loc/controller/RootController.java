package com.projet.yaounde_loc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<?> root() {
        return ResponseEntity.ok().body(new Object() {
            public final String message = "Yaounde Loc API";
            public final String version = "1.0.0";
            public final String[] endpoints = {
                "/auth/login - POST",
                "/auth/register - POST",
                "/annonces - GET",
                "/admin/dashboard - GET (Admin only)",
                "/swagger-ui.html - API Documentation"
            };
        });
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok().body(new Object() {
            public final String status = "UP";
            public final String service = "Yaounde Loc API";
        });
    }
}
