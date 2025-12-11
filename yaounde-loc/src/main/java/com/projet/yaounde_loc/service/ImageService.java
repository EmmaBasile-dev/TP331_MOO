package com.projet.yaounde_loc.service;

import com.projet.yaounde_loc.model.Annonce;
import com.projet.yaounde_loc.model.Image;
import com.projet.yaounde_loc.repository.AnnonceRepository;
import com.projet.yaounde_loc.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.AccessDeniedException;



@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private AnnonceRepository annonceRepository;
    @Autowired
    private FileStorageService fileStorageService;


    // Méthode pour l'upload sécurisé
    public Image uploadImage(Long annonceId, MultipartFile file, String emailProprietaire) {
        
        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée avec ID: " + annonceId));

        // ⭐ Vérification de sécurité : Seul le propriétaire de l'annonce peut ajouter des images
        if (!annonce.getProprietaire().getEmail().equals(emailProprietaire)) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à ajouter une image à cette annonce.");
        }
        
        // 1. Stocker le fichier physiquement
        String fileName = fileStorageService.storeFile(file);
        
        // 2. Sauvegarder les métadonnées dans la base de données
        Image image = Image.builder()
                .fileName(fileName)
                .filePath("/files/" + fileName) // Chemin public pour l'accès
                .fileType(file.getContentType())
                .annonce(annonce)
                .build();
        
        return imageRepository.save(image);
    }
}
