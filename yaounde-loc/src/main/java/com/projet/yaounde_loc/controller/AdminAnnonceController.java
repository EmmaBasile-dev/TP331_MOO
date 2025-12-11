package com.projet.yaounde_loc.controller;

import com.projet.yaounde_loc.model.Annonce;
import com.projet.yaounde_loc.service.AnnonceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// Tous les chemins ici commenceront par /admin/annonces
@RequestMapping("/admin/annonces") 
public class AdminAnnonceController {

    @Autowired
    private AnnonceService annonceService;

    // 1. ENDPOINT: GET /admin/annonces/pending (Lister les annonces en attente)
    @GetMapping("/pending")
    public ResponseEntity<List<Annonce>> getAnnoncesEnAttente() {
        List<Annonce> annonces = annonceService.getAnnoncesEnAttente();
        return ResponseEntity.ok(annonces);
    }

    // 2. ENDPOINT: PUT /admin/annonces/validate/{id} (Valider une annonce)
    @PutMapping("/validate/{id}")
    public ResponseEntity<Annonce> validerAnnonce(@PathVariable Long id) {
        Annonce annonceValidee = annonceService.validerAnnonce(id);
        return ResponseEntity.ok(annonceValidee);
    }

    // 3. ENDPOINT: PUT /admin/annonces/reject/{id} (Rejeter une annonce)
    @PutMapping("/reject/{id}")
    public ResponseEntity<Annonce> rejeterAnnonce(@PathVariable Long id) {
        Annonce annonceRejetee = annonceService.rejeterAnnonce(id);
        return ResponseEntity.ok(annonceRejetee);
    }
}
