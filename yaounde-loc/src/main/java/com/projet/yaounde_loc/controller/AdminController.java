package com.projet.yaounde_loc.controller;

import com.projet.yaounde_loc.model.Annonce;
import com.projet.yaounde_loc.model.StatutAnnonce;
import com.projet.yaounde_loc.service.AnnonceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // ❗ NOUVEAU
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/annonces")
public class AdminController {

    @Autowired
    private AnnonceService annonceService;

    // Endpoint pour obtenir toutes les annonces en attente
    @GetMapping("/en-attente")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // 🔒 Seul l'ADMIN peut accéder
    public ResponseEntity<List<Annonce>> getAnnoncesEnAttente() {
        List<Annonce> annonces = annonceService.getAnnoncesEnAttente();
        return ResponseEntity.ok(annonces);
    }

    // Endpoint pour VALIDER une annonce
    @PatchMapping("/{id}/valider")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // 🔒 Seul l'ADMIN peut accéder
    public ResponseEntity<Annonce> validerAnnonce(@PathVariable Long id) {
        Annonce annonceValidee = annonceService.updateAnnonceStatut(id, StatutAnnonce.VALIDEE);
        return ResponseEntity.ok(annonceValidee);
    }

    // Endpoint pour REJETER une annonce
    @PatchMapping("/{id}/rejeter")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // 🔒 Seul l'ADMIN peut accéder
    public ResponseEntity<Annonce> rejeterAnnonce(@PathVariable Long id) {
        Annonce annonceRejetee = annonceService.updateAnnonceStatut(id, StatutAnnonce.REJETEE);
        return ResponseEntity.ok(annonceRejetee);
    }
}
