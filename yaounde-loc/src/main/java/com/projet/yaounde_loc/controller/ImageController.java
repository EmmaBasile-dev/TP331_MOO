package com.projet.yaounde_loc.controller;

import com.projet.yaounde_loc.model.Image;
import com.projet.yaounde_loc.service.FileStorageService;
import com.projet.yaounde_loc.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.AccessDeniedException;


@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;
    @Autowired
    private FileStorageService fileStorageService;
    
    
   @PostMapping("/annonces/{annonceId}/upload-image")
    public ResponseEntity<?> uploadImage(
            @PathVariable Long annonceId,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {
        try {
            Image image = imageService.uploadImage(annonceId, file, authentication.getName());
            return new ResponseEntity<>(image, HttpStatus.CREATED);
            
        } catch (AccessDeniedException e) { 
            
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN); 
            
        } catch (RuntimeException e) { 
            
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); 
        }
    }
    
    // ⭐ 2. Endpoint pour Servir le Fichier (Public)
    // Permet aux navigateurs d'accéder aux images via l'URL /files/{fileName}
    @GetMapping("/files/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        String contentType = "application/octet-stream";
        // Tente de déterminer le type MIME du fichier (simplifié ici, peut être amélioré)
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
